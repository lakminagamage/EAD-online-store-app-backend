import { Router } from "express";
import axios from "axios";
import { config } from "../config";

const router = Router();

router.get("/", async (req, res) => {
  try {
    const response = await axios.get(`${config.userServiceUrl}/api/users`);
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

// Create a new user
router.post("/", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/api/users`,
      req.body
    );
    res.status(201).json(response.data);
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

// Get user by ID
router.get("/:id", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.userServiceUrl}/api/users/${req.params.id}`
    );
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

// Update user by ID
router.put("/:id", async (req, res) => {
  try {
    const response = await axios.put(
      `${config.userServiceUrl}/api/users/${req.params.id}`,
      req.body
    );
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

// Delete user by ID
router.delete("/:id", async (req, res) => {
  try {
    await axios.delete(`${config.userServiceUrl}/api/users/${req.params.id}`);
    res.status(204).send();
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

// Get user by email
router.get("/email", async (req, res) => {
  try {
    const response = await axios.get(
      `${config.userServiceUrl}/api/users/email`,
      { params: { email: req.query.email } }
    );
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ message: "User Service is unavailable" });
  }
});

export default router;
