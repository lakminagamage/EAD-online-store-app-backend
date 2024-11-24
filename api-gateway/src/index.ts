import express from "express";
import productsRoutes from "./routes/product";
import usersRoutes from "./routes/user";
import cors from "cors";
import ordersRoutes from "./routes/order";

const app = express();

app.use(cors());

app.use("/api/products", productsRoutes);
app.use("/api/users", usersRoutes);
app.use("/api/orders", ordersRoutes);

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
});
