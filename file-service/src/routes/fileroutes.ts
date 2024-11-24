import { Router } from "express";
import { uploadImage, downloadImage } from "../controller/fileController";

const router = Router();

router.post("/upload", uploadImage);
router.get("/download/:fileName", downloadImage);

export default router;
