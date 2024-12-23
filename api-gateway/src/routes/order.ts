import { Router } from "express";
import axios from "axios";
import { config } from "../config";
import authMiddleware from "../middleware/authMiddleware";

const router = Router();

router.get("/", async (req, res) => {
  try {
    const response = await axios.get(`${config.orderServiceUrl}/orders`);
    res.json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.post("/without-payment", async (req, res) => {
  try {
    const response = await axios.post(`${config.orderServiceUrl}/orders`, {
      userId: req.body.userId,
      status: "PENDING",
      items: req.body.items,
    });
    res.status(response.status).json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.post("/with-payment", authMiddleware, async (req, res) => {
  try {
    const response = await axios.post(`${config.orderServiceUrl}/orders`, {
      userId: req.body.userId,
      status: "PAID",
      items: req.body.items,
    });

    try {
      await axios.post(`${config.paymentServiceUrl}/payments`, {
        orderId: response.data.id,
        paymentType: req.body.paymentType,
      });
    } catch (error) {
      await axios.delete(
        `${config.orderServiceUrl}/orders/${response.data.id}`
      );
      throw error;
    }

    res.status(response.status).json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.delete("/:orderId", authMiddleware, async (req, res) => {
  try {
    const response = await axios.delete(
      `${config.orderServiceUrl}/orders/${req.params.orderId}`
    );

    try {
      await axios.delete(
        `${config.paymentServiceUrl}/payments/order/${req.params.orderId}`
      );
    } catch (error) { }

    console.log(response.data);

    res.status(204).send();
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.get("/:orderId", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.orderServiceUrl}/orders/${req.params.orderId}`
    );

    const order = response.data;
    order.items = await getOrderProducts(order.items);
    order.user = await getUserDetails(order.userId);

    try {
      order.payment = await getPaymentDetails(order.id);
    } catch (error) {
      order.payment = null;
    }

    res.json(order);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.get("/user/:userId", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.orderServiceUrl}/orders/user/${req.params.userId}`
    );
    const orders = response.data;

    for (const order of orders) {
      if (order.items.length > 0) {
        order.items = await getOrderProducts(order.items);
      }

      try {
        order.payment = await getPaymentDetails(order.id);
      } catch (error) {
        order.payment = null;
      }
    }

    res.json(orders);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.patch("/:orderId/payment-status", authMiddleware, async (req, res) => {
  try {
    await axios.patch(
      `${config.orderServiceUrl}/orders/${req.params.orderId}/payment-status`,
      null,
      {
        params: { status: req.query.status },
      }
    );
    res.status(204).send();
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

async function getOrderProducts(items: any[]) {
  const productIds = items.map((item: any) => item.productId).join(",");
  const productResponse = await axios.get(
    `${config.productServiceUrl}/products/by-ids/`,
    {
      params: { product_ids: productIds },
    }
  );

  return items.map((item: any) => {
    const product = productResponse.data.find(
      (product: any) => product.ID === item.productId
    );

    if (product) {
      return {
        ...item,
        product: product,
      };
    }
  });
}

async function getUserDetails(userId: string) {
  const response = await axios.get(`${config.userServiceUrl}/users/${userId}`);
  return response.data;
}

async function getPaymentDetails(orderId: string) {
  const response = await axios.get(
    `${config.paymentServiceUrl}/payments/order/${orderId}`
  );
  return response.data;
}

export default router;
