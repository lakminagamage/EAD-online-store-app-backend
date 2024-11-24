FROM golang:1.22-alpine as builder
WORKDIR /app
COPY go.mod ./
COPY go.sum ./
RUN go mod tidy
COPY . .
RUN go build -o product-service main.go

FROM alpine:latest
WORKDIR /app
COPY --from=builder /app/product-service /app/product-service
EXPOSE 8081
CMD ["./product-service"]
