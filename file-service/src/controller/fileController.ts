import { Request, Response } from "express";
import bucket from "../config/firebase";
import multer from "multer";

const upload = multer({ storage: multer.memoryStorage() });

export const uploadFile = [
  upload.single("file"),
  async (req: Request, res: Response): Promise<void> => {
    try {
      if (req.method !== "POST") {
        res.status(400).json({ message: "Method not allowed" });
        return;
      }

      if (!req.file) {
        res.status(400).json({ message: "No file uploaded" });
        return;
      }

      const file = req.file;
      const blob = bucket.file(file.originalname);
      const blobStream = blob.createWriteStream({
        metadata: {
          contentType: file.mimetype,
        },
        predefinedAcl: "publicRead", // Add this line to set public access
      });

      blobStream.on("error", (err) => {
        res.status(500).json({ error: err.message });
      });

      blobStream.on("finish", async () => {
        const publicUrl = `https://storage.googleapis.com/${bucket.name}/${blob.name}`;
        res.status(200).json({ url: publicUrl });
      });

      blobStream.end(file.buffer);
    } catch (err) {
      res.status(500).json({ error: (err as Error).message });
    }
  },
];

export const downloadFile = async (
  req: Request,
  res: Response
): Promise<void> => {
  const { fileName } = req.body;

  if (!fileName) {
    res.status(400).json({ message: "File name is required" });
    return;
  }

  console.log(fileName);

  try {
    const file = bucket.file(fileName as string); // Type assertion to string
    const [exists] = await file.exists();

    if (!exists) {
      res.status(404).json({ message: "File not found" });
      return; // Add return to stop further execution
    }

    res.setHeader("Content-Disposition", `attachment; filename=${fileName}`);
    const stream = file.createReadStream();

    stream.on("error", (err) => {
      res.status(500).json({ error: err.message });
    });

    stream.pipe(res);
  } catch (err) {
    res.status(500).json({ error: (err as Error).message });
  }
};
