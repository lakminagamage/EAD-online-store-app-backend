import { Request, Response } from "express";
import bucket from "../config/firebase";
import multer from "multer";
import { v4 as uuidv4 } from "uuid"; // Add this line to import uuid

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
      const uniqueName = `${uuidv4()}_${file.originalname}`;
      const blob = bucket.file(uniqueName);
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

export const UploadMultipleFiles = [
  upload.array("files"),
  async (req: Request, res: Response): Promise<void> => {
    try {
      if (req.method !== "POST") {
        res.status(400).json({ message: "Method not allowed" });
        return;
      }

      if (!req.files || (req.files as Express.Multer.File[]).length === 0) {
        res.status(400).json({ message: "No files uploaded" });
        return;
      }

      const files = req.files as Express.Multer.File[];
      const urls: string[] = [];

      for (const file of files) {
        // Generate unique name and hash the file name
        const uniqueName = `${uuidv4()}_${file.originalname}`;
        const blob = bucket.file(uniqueName);

        const blobStream = blob.createWriteStream({
          metadata: {
            contentType: file.mimetype,
          },
          predefinedAcl: "publicRead",
        });

        blobStream.on("error", (err) => {
          res.status(500).json({ error: err.message });
        });

        blobStream.on("finish", async () => {
          const publicUrl = `https://storage.googleapis.com/${bucket.name}/${blob.name}`;
          urls.push(publicUrl);

          console.log(publicUrl);

          if (urls.length === files.length) {
            res.status(200).json({ urls });
          }
        });

        blobStream.end(file.buffer);
      }
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

export const deleteMultipleFiles = async (
  req: Request,
  res: Response
): Promise<void> => {
  if (!req.body.files) {
    res.status(400).json({ message: "Files are required" });
    return;
  }

  const files = req.body.files as string[];

  if (files.length === 0) {
    res.status(400).json({ message: "No files to delete" });
    return;
  }

  try {
    for (const file of files) {
      const fileName = removePublicUrl(file);
      const blob = bucket.file(fileName);
      const [exists] = await blob.exists();

      if (!exists) {
        res.status(404).json({ message: "File not found" });
        return; // Add return to stop further execution
      }

      await blob.delete();
    }

    res.status(200).json({ message: "Files deleted successfully" });
  } catch (err) {
    res.status(500).json({ error: (err as Error).message });
  }
};

export const deleteFile = async (
  req: Request,
  res: Response
): Promise<void> => {
  if (req.method !== "DELETE") {
    res.status(400).json({ message: "Method not allowed" });
    return;
  }

  if (!req.query.fileName) {
    res.status(400).json({ message: "File name is required" });
    return;
  }

  const fileName = removePublicUrl(req.query.fileName as string);

  if (!fileName) {
    res.status(400).json({ message: "File name is required" });
    return;
  }

  try {
    const file = bucket.file(fileName as string); // Type assertion to string
    const [exists] = await file.exists();

    if (!exists) {
      res.status(404).json({ message: "File not found" });
      return; // Add return to stop further execution
    }

    await file.delete();
    res.status(200).json({ message: "File deleted successfully" });
  } catch (err) {
    res.status(500).json({ error: (err as Error).message });
  }
};

const removePublicUrl = (url: string): string => {
  const parts = url.split("/");
  return parts[parts.length - 1];
};
