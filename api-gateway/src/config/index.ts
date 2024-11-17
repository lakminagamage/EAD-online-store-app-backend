import dotenv from "dotenv";

dotenv.config();

export const config = {
  productServiceUrl: process.env.PRODUCT_SERVICE_URL as string,
  orderServiceUrl: process.env.ORDER_SERVICE_URL as string,
  userServiceUrl: process.env.USER_SERVICE_URL as string,
};
