import { Router } from "express";
import axios from "axios";
import { config } from "../config";

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

router.post("/", async (req, res) => {
  try {
    const response = await axios.post(`${config.orderServiceUrl}/orders`, {
      userId: req.body.userId,
      status: req.body.status,
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

router.delete("/:orderId", async (req, res) => {
  try {
    await axios.delete(
      `${config.orderServiceUrl}/orders/${req.params.orderId}`
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

router.get("/:orderId", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.orderServiceUrl}/orders/${req.params.orderId}`
    );

    const order = response.data;
    order.items = await getOrderProducts(order.items);

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
      order.items = await getOrderProducts(order.items);
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

router.patch("/:orderId/payment-status", async (req, res) => {
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

export default router;
