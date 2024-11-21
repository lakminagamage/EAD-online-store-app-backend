package main

import (
	"log"
	"net/http"
	"os"
	"product-service/handlers"

	"product-service/models" // Uncomment this line to migrate the schema
	"product-service/pkg/database"

	"github.com/gorilla/mux"
)

func main() {
    database.Connect()

    // Migrate the schema if needed
    database.DB.AutoMigrate(&models.Product{}, &models.ProductType{}, &models.ProductImage{})

    r := mux.NewRouter()

    // product controller routes
    r.HandleFunc("/products/", handlers.GetAllProducts).Methods("GET")
    r.HandleFunc("/products/{id}", handlers.GetProductByID).Methods("GET")
    r.HandleFunc("/products/", handlers.CreateProduct).Methods("POST")
    r.HandleFunc("/products/{id}", handlers.UpdateProduct).Methods("PUT")
    r.HandleFunc("/products/{id}", handlers.DeleteProduct).Methods("DELETE")
    r.HandleFunc("/products/{id}/stock", handlers.UpdateStock).Methods("PATCH")
    r.HandleFunc("/products/search/", handlers.SearchProducts).Methods("GET")
    r.HandleFunc("/products/by-ids/", handlers.GetProductsByIDs).Methods("GET")
    r.HandleFunc("/products/by-type/{id}", handlers.GetAllProductsByProductType).Methods("GET")

    // product-type controller routes
    r.HandleFunc("/products/types/", handlers.GetAllProductTypes).Methods("GET")
    r.HandleFunc("/products/types/", handlers.CreateProductType).Methods("POST")
    r.HandleFunc("/products/types/{id}", handlers.DeleteProductType).Methods("DELETE")

    // get the port from the env file
    port := os.Getenv("PORT")
    log.Println("Server starting on port " + port)
    log.Fatal(http.ListenAndServe(":" + port, r))
}
