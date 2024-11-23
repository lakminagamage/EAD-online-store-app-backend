import admin from "firebase-admin";
import { ServiceAccount } from "firebase-admin";

const serviceAccount: ServiceAccount = require("../../path/to/your-firebase-credentials.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  storageBucket: "your-project-id.appspot.com",
});

const bucket = admin.storage().bucket();

export { bucket };
