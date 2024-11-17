import { Router } from "express";
import axios from "axios";
import { config } from "../config";

const router = Router();

router.get("/", async (req, res) => {
  try {
    const response = await axios.get(`${config.orderServiceUrl}/orders`);
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ message: "Order Service is unavailable" });
  }
});

export default router;
