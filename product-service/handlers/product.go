package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"product-service/models"
	"product-service/pkg/database"

	"github.com/gorilla/mux"
)

func GetAllProducts(w http.ResponseWriter, r *http.Request) {
    var products []models.Product
    if err := database.DB.Find(&products).Error; err != nil {
        log.Println("Error retrieving products:", err)
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }
    
    log.Println("Products retrieved:", products)
    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(products)
}


func GetProductByID(w http.ResponseWriter, r *http.Request) {
    params := mux.Vars(r)
    var product models.Product
    if err := database.DB.First(&product, params["id"]).Error; err != nil {
        http.Error(w, "Product not found", http.StatusNotFound)
        return
    }
    json.NewEncoder(w).Encode(product)
}

func CreateProduct(w http.ResponseWriter, r *http.Request) {
    var product models.Product
    if err := json.NewDecoder(r.Body).Decode(&product); err != nil {
        http.Error(w, "Invalid input", http.StatusBadRequest)
        return
    }
    database.DB.Create(&product)
    json.NewEncoder(w).Encode(product)
}

func UpdateProduct(w http.ResponseWriter, r *http.Request) {
    params := mux.Vars(r)
    var product models.Product
    if err := database.DB.First(&product, params["id"]).Error; err != nil {
        http.Error(w, "Product not found", http.StatusNotFound)
        return
    }
    if err := json.NewDecoder(r.Body).Decode(&product); err != nil {
        http.Error(w, "Invalid input", http.StatusBadRequest)
        return
    }
    database.DB.Save(&product)
    json.NewEncoder(w).Encode(product)
}

func DeleteProduct(w http.ResponseWriter, r *http.Request) {
    params := mux.Vars(r)
    var product models.Product
    if err := database.DB.First(&product, params["id"]).Error; err != nil {
        http.Error(w, "Product not found", http.StatusNotFound)
        return
    }
    database.DB.Delete(&product)
    w.WriteHeader(http.StatusNoContent)
}
