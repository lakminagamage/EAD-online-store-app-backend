import { Router } from "express";
import axios from "axios";
import { config } from "../config";

const router = Router();

router.get("/", async (req, res) => {
  try {
    const response = await axios.get(`${config.userServiceUrl}/users`);
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

export default router;
