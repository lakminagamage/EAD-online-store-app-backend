import { Router } from "express";
import multer from "multer";
import { uploadImage, downloadImage } from '../controller/fileController';

const router = Router();
const upload = multer({ storage: multer.memoryStorage() });

router.post("/upload", upload.single("file"), uploadImage);
//router.get("/download/:fileName", downloadImage);
router.get("/ashan", (req, res) => {
    res.send("Hello, Ashan! This is your GET endpoint.");
});

export default router;
