import express, { Application } from "express";
import fileroutes from "./routes/fileroutes";

const app: Application = express();
const PORT = process.env.PORT || 8085;

app.use(express.json());
app.use("/files", fileroutes);

app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});
