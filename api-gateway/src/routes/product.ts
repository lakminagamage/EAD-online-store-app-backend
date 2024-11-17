import express from "express";
import { createProxyMiddleware } from "http-proxy-middleware";
import { config } from "../config";

const router = express.Router();

// Proxy for Product-related routes
router.use(
  "/products",
  createProxyMiddleware({
    target: config.productServiceUrl,
    changeOrigin: true,
    pathRewrite: (path, req) => {
      // if path does nor contain /products/ ignore it otherwise change the path
      console.log(`Path: ${path}`);
      return `/products/${path}`;
      // if (!path.includes("/products/")) {
      //   return path;
      // }
      // return `/products/${path}`;
    },
    on: {
      proxyReq: (proxyReq, req, res) => {
        console.log(
          `Proxying request to: ${config.productServiceUrl}${proxyReq.path}, Method: ${proxyReq.method}`
        );
        console.log(`Original URL: ${(req as express.Request).originalUrl}`);
        console.log(`Rewritten URL: ${proxyReq.path}\n`);
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
