import { Router } from "express";
import axios from "axios";
import { config } from "../config";

const router = Router();

router.post("/", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.paymentServiceUrl}/payments`,
      req.body
    );
    res.status(response.status).json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.get("/:id", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.paymentServiceUrl}/payments/${req.params.id}`
    );
    res.json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.get("/order/:orderId", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.paymentServiceUrl}/payments/order/${req.params.orderId}`
    );
    res.json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.put("/:id", async (req, res) => {
  try {
    const response = await axios.put(
      `${config.paymentServiceUrl}/payments/${req.params.id}`,
      req.body
    );
    res.status(response.status).json(response.data);
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

router.delete("/:id", async (req, res) => {
  try {
    await axios.delete(`${config.paymentServiceUrl}/payments/${req.params.id}`);
    res.status(204).send();
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json((error as Error).message);
    }
  }
});

export default router;
