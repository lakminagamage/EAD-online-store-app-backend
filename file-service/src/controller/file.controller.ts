import { Request, Response } from "express";
import { bucket } from "../config/firebase";
import multer from "multer";

// Configure Multer for in-memory file uploads
const upload = multer({
  storage: multer.memoryStorage(),
  limits: { fileSize: 5 * 1024 * 1024 }, // Limit to 5MB
});

// Upload Image to Firebase
const uploadFile = async (req: Request, res: Response): Promise<void> => {
  if (!req.file) {
    res.status(400).send("No file uploaded.");
    return;
  }

  const file = bucket.file(req.file.originalname);

  const stream = file.createWriteStream({
    metadata: {
      contentType: req.file.mimetype,
    },
  });

  stream.on("error", (error) => {
    console.error(error);
    return res.status(500).send("Error uploading file.");
  });

  stream.on("finish", async () => {
    const publicUrl = `https://storage.googleapis.com/${bucket.name}/${file.name}`;
    res.status(200).send({ url: publicUrl });
  });

  stream.end(req.file.buffer);
};

// Download Image from Firebase
const downloadFile = async (req: Request, res: Response): Promise<void> => {
  const { filename } = req.params;

  const file = bucket.file(filename);

  try {
    const [exists] = await file.exists();
    if (!exists) {
      res.status(404).send("File not found.");
      return;
    }

    res.setHeader("Content-Type", "application/octet-stream");
    file.createReadStream().pipe(res);
  } catch (error) {
    console.error(error);
    res.status(500).send("Error downloading file.");
  }
};

export { upload, uploadFile, downloadFile };
