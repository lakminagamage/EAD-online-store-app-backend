import { Router } from "express";
import { uploadFile, downloadFile } from "../controller/fileController";

const router = Router();

router.post("/upload", uploadFile);
router.post("/download", downloadFile); // Changed from GET to POST

export default router;
