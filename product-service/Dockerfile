# Use the official Go image as the base image
FROM golang:1.20-alpine

# Set the Current Working Directory inside the container
WORKDIR /app

# Copy go.mod and go.sum files to the container if they exist (avoids re-initialization)
COPY go.mod ./
COPY go.sum ./

# Download Go module dependencies (only if go.mod/go.sum exist)
RUN go mod tidy

# Copy the rest of the application code to the container
COPY . .

# Build the Go app
RUN go build -o product-service main.go

# Expose the service on port 8081
EXPOSE 8081

# Command to run the built Go application
CMD ["./product-service"]
