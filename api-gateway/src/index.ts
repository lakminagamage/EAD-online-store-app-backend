import express from "express";
import productsRoutes from "./routes/product";
import usersRoutes from "./routes/user";
import cors from "cors";

const app = express();

app.use(cors());

// Use the products router for any `/api/products` routes
app.use("/api/products", productsRoutes);
app.use("/api/users", usersRoutes);

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
});
