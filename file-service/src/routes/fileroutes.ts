import { Router } from "express";
import {
  uploadFile,
  downloadFile,
  UploadMultipleFiles,
} from "../controller/fileController";

const router = Router();

router.post("/upload", uploadFile);
router.post("/upload/multiple", UploadMultipleFiles);
router.post("/download", downloadFile);

export default router;
