# Use the official Go image as the base image
FROM golang:1.20-alpine

# Install PostgreSQL client for interacting with PostgreSQL databases
RUN apk add --no-cache postgresql-client

# Set the Current Working Directory inside the container
WORKDIR /app

# Copy go.mod and go.sum files to the container if they exist (avoids re-initialization)
COPY go.mod ./
COPY go.sum ./

# Download Go module dependencies (only if go.mod/go.sum exist)
RUN if [ -f go.mod ]; then go mod download; fi

# Copy the rest of the application code to the container
COPY . .

# Build the Go app
RUN go build -o product-service ./cmd/main.go

# Expose the service on port 8000
EXPOSE 8000

# Set default environment variables for PostgreSQL (can be overridden in Docker Compose or runtime)
ENV DB_HOST=localhost \
    DB_USER=sampleuser \
    DB_PASSWORD=samplepassword \
    DB_NAME=sampledb \
    DB_PORT=5432

# Command to run the built Go application
CMD ["./product-service"]
