# Build the app
FROM node:18-alpine AS build
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Run the app
FROM node:18 AS runner
WORKDIR /usr/src/app
COPY firebase-service-account.json ./
COPY package*.json ./
RUN npm install --production
COPY --from=build /usr/src/app/dist ./dist
EXPOSE 8085
CMD ["node", "dist/index.js"]
