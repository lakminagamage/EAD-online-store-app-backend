import express from "express";
import bodyParser from "body-parser";
import fileRoutes from "./routes/file.routes";

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.json());
app.use("/files", fileRoutes);

app.listen(PORT, () => {
  console.log(`File Service is running on http://localhost:${PORT}`);
});
