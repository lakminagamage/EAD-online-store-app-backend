package main

import (
	"log"
	"net/http"
	"os"
	"product-service/handlers"

	//"product-service/models" // Uncomment this line to migrate the schema
	"product-service/pkg/database"

	"github.com/gorilla/mux"
)

func main() {
    database.Connect()

    // Migrate the schema if needed
    //database.DB.AutoMigrate(&models.Product{}, &models.ProductType{})

    r := mux.NewRouter()

    // product controller routes
    r.HandleFunc("/products", handlers.GetAllProducts).Methods("GET")
    r.HandleFunc("/products/{id}", handlers.GetProductByID).Methods("GET")
    r.HandleFunc("/products/product-type/{id}", handlers.GetAllProductsByProductType).Methods("GET")
    r.HandleFunc("/products", handlers.CreateProduct).Methods("POST")
    r.HandleFunc("/products/{id}", handlers.UpdateProduct).Methods("PUT")
    r.HandleFunc("/products/{id}", handlers.DeleteProduct).Methods("DELETE")
    r.HandleFunc("/products-search", handlers.SearchProducts).Methods("GET")
    r.HandleFunc("/products-by-ids", handlers.GetProductsByIDs).Methods("GET")
    r.HandleFunc("/products/{id}/stock", handlers.UpdateStock).Methods("PATCH")

    // product-type controller routes
    r.HandleFunc("/product-types", handlers.GetAllProductTypes).Methods("GET")
    r.HandleFunc("/product-types", handlers.CreateProductType).Methods("POST")
    r.HandleFunc("/product-types/{id}", handlers.DeleteProductType).Methods("DELETE")


    // get the port from the env file
    port := os.Getenv("PORT")
    log.Println("Server starting on port " + port)
    log.Fatal(http.ListenAndServe(":" + port, r))
}
