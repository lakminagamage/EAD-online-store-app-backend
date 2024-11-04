package main

import (
    "log"
    "net/http"
    "product-service/handlers"
    "product-service/pkg/database"
    "product-service/models"
    "github.com/gorilla/mux"
)

func main() {
    database.Connect()
    database.DB.AutoMigrate(&models.Product{})

    r := mux.NewRouter()
    r.HandleFunc("/products", handlers.GetAllProducts).Methods("GET")
    r.HandleFunc("/products/{id}", handlers.GetProductByID).Methods("GET")
    r.HandleFunc("/products", handlers.CreateProduct).Methods("POST")
    r.HandleFunc("/products/{id}", handlers.UpdateProduct).Methods("PUT")
    r.HandleFunc("/products/{id}", handlers.DeleteProduct).Methods("DELETE")

    log.Println("Server starting on port 8000")
    log.Fatal(http.ListenAndServe(":8000", r))
}
