import { Router } from "express";
import axios from "axios";
import { config } from "../config";
import authMiddleware from "../middleware/authMiddleware";

const router = Router();

// Get user by email
router.get("/email", authMiddleware, async (req, res) => {
  try {
    const response = await axios.get(`${config.userServiceUrl}/users/email`, {
      params: { email: req.query.email },
    });
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// user controller
// get all users
router.get("/", authMiddleware, async (req, res) => {
  try {
    const response = await axios.get(`${config.userServiceUrl}/users`);
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Create a new user
router.post("/", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/users`,
      req.body
    );
    res.status(201).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Get user by ID
router.get("/:id", authMiddleware, async (req, res) => {
  try {
    const response = await axios.get(
      `${config.userServiceUrl}/users/${req.params.id}`
    );
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Update user by ID
router.put("/:id", authMiddleware, async (req, res) => {
  try {
    const response = await axios.put(
      `${config.userServiceUrl}/users/${req.params.id}`,
      req.body
    );
    res.json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Delete user by ID
router.delete("/:id", authMiddleware, async (req, res) => {
  try {
    await axios.delete(`${config.userServiceUrl}/users/${req.params.id}`);
    res.status(204).send();
  } catch (error) {
    res.status(500).json({ error });
  }
});

// auth controller
// Register a new user
router.post("/auth/register", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/auth/register`,
      req.body
    );
    res.status(201).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Login user
router.post("/auth/login", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/auth/login`,
      req.body
    );
    res.status(200).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Verify token
router.post("/auth/verify-token", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/auth/verify-token`,
      req.body
    );
    res.status(200).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Send password reset email
router.post("/auth/send-password-reset-email", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/auth/send-password-reset-email`,
      req.body
    );
    res.status(200).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Reset password
router.post("/auth/reset-password", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/auth/reset-password`,
      req.body
    );
    res.status(200).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

// Verify OTP
router.post("/auth/verify-otp", async (req, res) => {
  try {
    const response = await axios.post(
      `${config.userServiceUrl}/auth/verify-otp`,
      req.body
    );
    res.status(200).json(response.data);
  } catch (error) {
    res.status(500).json({ error });
  }
});

export default router;
