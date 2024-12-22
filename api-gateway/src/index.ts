import express from "express";
import productsRoutes from "./routes/product";
import usersRoutes from "./routes/user";
import cors from "cors";
import ordersRoutes from "./routes/order";
import paymentRoutes from "./routes/payment";
import cartRoutes from "./routes/cart";

const app = express();

app.use(cors());
app.use(express.json());

app.use("/api/products", productsRoutes);
app.use("/api/users", usersRoutes);
app.use("/api/orders", ordersRoutes);
app.use("/api/payments", paymentRoutes);
app.use("/api/carts", cartRoutes);

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`API Gateway running on port ${PORT}`);
});
