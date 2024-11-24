import admin from "firebase-admin";
import path from "path";

const serviceAccount = require(path.resolve("firebase-service-account.json"));

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  storageBucket: "gs://hypercube-cf9d2.firebasestorage.app",
});

const bucket = admin.storage().bucket();
export default bucket;
