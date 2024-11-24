import { Request, Response } from "express";
import bucket from "../config/firebase";

export const uploadImage = async (
  req: Request,
  res: Response
): Promise<void> => {
  try {
    console.log(req.file);
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
};

export const downloadImage = async (req: Request, res: Response) => {
  const { fileName } = req.params;

  try {
    const file = bucket.file(fileName);
    const [exists] = await file.exists();

    if (!exists) {
      return res.status(404).json({ message: "File not found" });
    }

    const stream = file.createReadStream();
    stream.pipe(res);
  } catch (err) {
    res.status(500).json({ error: err });
  }
};
