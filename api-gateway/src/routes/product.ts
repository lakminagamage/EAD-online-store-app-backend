import express from "express";
import { createProxyMiddleware } from "http-proxy-middleware";
import { config } from "../config";
import multer from "multer";
import axios from "axios";
import FormData from "form-data";

const router = express.Router();
const upload = multer({ storage: multer.memoryStorage() });

router.post(
  "/files",
  upload.array("images"),
  async (req, res): Promise<void> => {
    try {
      if (req.method !== "POST") {
        res.status(400).json({ message: "Method not allowed" });
        return;
      }

      const files = req.files as Express.Multer.File[];
      if (!files || files.length === 0) {
        res.status(400).json({ message: "No images uploaded" });
      }

      const formData = new FormData();
      files.forEach((file) => {
        formData.append("files", file.buffer, file.originalname);
      });

      const uploadResponse = await axios.post(
        `${config.fileServiceUrl}/files/upload/multiple`,
        formData,
        {
          headers: {
            ...formData.getHeaders(),
          },
        }
      );

      const imageUrls = uploadResponse.data.urls.map((url: string) => ({
        url,
      }));

      const productData = {
        name: req.body.name,
        description: req.body.description,
        price: parseFloat(req.body.price),
        stock: parseInt(req.body.stock),
        product_type_id: parseInt(req.body.product_type_id),
        images: imageUrls,
      };

      const productResponse = await axios.post(
        `${config.productServiceUrl}/products/`,
        productData
      );

      res.status(productResponse.status).json(productResponse.data);
    } catch (error) {
      console.error("Error creating product:", error);
      res.status(500).json({ message: "Internal server error" });
    }
    return;
  }
);

// Proxy for Product-related routes
router.use(
  "/",
  createProxyMiddleware({
    target: config.productServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) => {
      return `/products/${path}`;
    },
    on: {
      proxyReq: (proxyReq, req, res) => {
        console.log(
          `Proxying request to: ${config.productServiceUrl}${proxyReq.path}, Method: ${proxyReq.method}`
        );
      },
      error: (err, req, res) => {
        console.error(
          `Error proxying request to: ${config.productServiceUrl}${
            (req as express.Request).originalUrl
          }`,
          err
        );
        (res as express.Response).status(500).send("Proxy error");
      },
    },
  })
);

export default router;
