import { Router } from "express";
import {
  uploadFile,
  downloadFile,
  UploadMultipleFiles,
  deleteFile,
  deleteMultipleFiles,
} from "../controller/fileController";

const router = Router();

router.post("/upload", uploadFile);
router.post("/upload/multiple", UploadMultipleFiles);
router.delete("/delete", deleteFile);
router.delete("/delete/multiple", deleteMultipleFiles);
router.post("/download", downloadFile);

export default router;
