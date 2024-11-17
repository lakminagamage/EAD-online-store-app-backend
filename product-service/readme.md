# Product Service

The **Product Service** is a microservice within an online shopping application. It provides CRUD (Create, Read, Update, Delete) operations for managing products and is built using Go and MySQL. The service is designed to run in a Docker environment and can be easily integrated with other services, like User and Order, which are implemented in Spring Boot.

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Setup](#setup)
  - [Requirements](#requirements)
  - [Environment Variables](#environment-variables)
  - [Docker Installation](#docker-installation)
- [Endpoints](#endpoints)
  - [Request and Response Examples](#request-and-response-examples)
- [Usage](#usage)
  - [Example Commands](#example-commands)
- [License](#license)

## Features

- RESTful API design
- Endpoints for creating, retrieving, updating, and deleting products
- MySQL database integration for product data persistence

## Project Structure

```plaintext
product-service/
├── main.go                  # Main entry point for the application
├── pkg/
│   └── database/
│       └── database.go      # Database connection setup
├── models/
│   └── product.go           # Product model definition
├── handlers/
│   └── product.go           # Handlers for product endpoints
├── Dockerfile               # Dockerfile for building the Go application
├── docker-compose.yml       # Docker Compose file for service orchestration
└── go.mod                   # Go module file
```

## Setup

### Requirements

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Environment Variables

The application requires several environment variables to connect to the MySQL database. These are defined in the `docker-compose.yml` file:

- `DB_HOST`: Hostname for the MySQL container (`db`)
- `DB_USER`: Username for MySQL (`root`)
- `DB_PASSWORD`: Password for MySQL (`password`)
- `DB_NAME`: Name of the database (`productdb`)

### Docker Installation

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd product-service
   ```

2. Build and start the services using Docker Compose:

   ```bash
   docker-compose up --build
   ```

   This command will:

   - Build the Go application image.
   - Start a MySQL database container.
   - Start the Product Service container, which will connect to the database.

3. Access the Product Service API at [http://localhost:8000](http://localhost:8000).

## Endpoints

The `Product Service` provides the following API endpoints:

| Method | Endpoint         | Description                |
| ------ | ---------------- | -------------------------- |
| GET    | `/products`      | Retrieve all products      |
| GET    | `/products/{id}` | Retrieve a product by ID   |
| POST   | `/products`      | Create a new product       |
| PUT    | `/products/{id}` | Update an existing product |
| DELETE | `/products/{id}` | Delete a product           |

### Request and Response Examples

#### Get All Products

- **Request**: `GET /products`
- **Response**:
  ```json
  [
    {
      "ID": 1,
      "Name": "Sample Product",
      "Description": "This is a sample product",
      "Price": 19.99,
      "Stock": 100
    }
  ]
  ```

#### Create a Product

- **Request**: `POST /products`
- **Body**:
  ```json
  {
    "name": "New Product",
    "description": "A new product description",
    "price": 29.99,
    "stock": 50
  }
  ```
- **Response**:
  ```json
  {
    "ID": 2,
    "Name": "New Product",
    "Description": "A new product description",
    "Price": 29.99,
    "Stock": 50
  }
  ```

#### Delete a Product

- **Request**: `DELETE /products/{id}`
- **Response**: `204 No Content`

## Usage

To interact with the Product Service, use a REST client like [Postman](https://www.postman.com/) or `curl` commands in your terminal.

### Example Commands

- **Get all products**:

  ```bash
  curl http://localhost:8000/products
  ```

- **Create a new product**:

  ```bash
  curl -X POST http://localhost:8000/products -d '{"name":"Sample Product","description":"This is a sample product","price":19.99,"stock":100}' -H "Content-Type: application/json"
  ```

- **Update a product**:
  ```bash
  curl -X PUT http://localhost:8000/products/1 -d '{"name":"Updated Product","price":39.99}' -H "Content-Type: application/json"
  ```

## License

This project is licensed under the MIT License.
