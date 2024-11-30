import { Router } from "express";
import axios from "axios";
import { config } from "../config";
import authMiddleware from "../middleware/authMiddleware";

const router = Router();

router.post("/", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.cartServiceUrl}/carts`,
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

router.get("/user/:userId", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.cartServiceUrl}/carts/user/${req.params.userId}`
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

router.put("/user/:userId", authMiddleware, async (req, res) => {
  try {
    const response = await axios.put(
      `${config.cartServiceUrl}/carts/user/${req.params.userId}`,
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

export default router;
