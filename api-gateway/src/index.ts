import express from "express";
import productsRoutes from "./routes/product";

const app = express();

// Use the products router for any `/api/products` routes
app.use("/api", productsRoutes);

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
});
