import express from "express";
import { createProxyMiddleware } from "http-proxy-middleware";
import { config } from "../config";
import multer from "multer";
import axios from "axios";
import FormData from "form-data";

const router = express.Router();
const upload = multer({ storage: multer.memoryStorage() });

const uploadFiles = async (files: Express.Multer.File[]) => {
  const formData = new FormData();
  files.forEach((file) => {
    formData.append(
      "files",
      file.buffer,
      file.originalname.replace(/\s/g, "_")
    );
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

  return uploadResponse.data.urls.map((url: string) => ({ url }));
};

const createOrUpdateProduct = async (
  method: string,
  productData: any,
  productId?: string
) => {
  if (method === "POST") {
    console.log(`Proxying request to: ${config.productServiceUrl}/products/`);
    return await axios.post(
      `${config.productServiceUrl}/products/`,
      productData
    );
  } else if (method === "PUT" && productId) {
    console.log(
      `Proxying request to: ${config.productServiceUrl}/products/${productId}`
    );
    return await axios.put(
      `${config.productServiceUrl}/products/${productId}`,
      productData
    );
  }
};

router.post("/", upload.array("images"), async (req, res): Promise<void> => {
  try {
    const files = req.files as Express.Multer.File[];
    if (!files || files.length === 0) {
      res.status(400).json({ message: "No images uploaded" });
      return;
    }

    const imageUrls = await uploadFiles(files);

    const productData = {
      name: req.body.name,
      description: req.body.description,
      price: parseFloat(req.body.price),
      stock: parseInt(req.body.stock),
      product_type_id: parseInt(req.body.product_type_id),
      images: imageUrls,
    };

    const productResponse = await createOrUpdateProduct("POST", productData);

    if (productResponse) {
      res.status(productResponse.status).json(productResponse.data);
    } else {
      res.status(500).json({ message: "Internal server error" });
    }
  } catch (error) {
    console.error("Error creating product:", error);
    res.status(500).json({ message: "Internal server error" });
  }
});

router.put("/:id", upload.array("images"), async (req, res): Promise<void> => {
  try {
    const files = req.files as Express.Multer.File[];
    if (!files || files.length === 0) {
      res.status(400).json({ message: "No images uploaded" });
      return;
    }

    const imageUrls = await uploadFiles(files);

    const productData = {
      name: req.body.name,
      description: req.body.description,
      price: parseFloat(req.body.price),
      stock: parseInt(req.body.stock),
      product_type_id: parseInt(req.body.product_type_id),
      images: imageUrls,
    };

    const productResponse = await createOrUpdateProduct(
      "PUT",
      productData,
      req.params.id
    );

    if (productResponse) {
      res.status(productResponse.status).json(productResponse.data);
    } else {
      res.status(500).json({ message: "Internal server error" });
    }
  } catch (error) {
    console.error("Error updating product:", error);
    res.status(500).json({ message: "Internal server error" });
  }
});

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
