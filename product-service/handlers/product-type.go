package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"product-service/models"
	"product-service/pkg/database"

	"github.com/gorilla/mux"
)

func GetAllProductTypes(w http.ResponseWriter, r *http.Request) {
	var productTypes []models.ProductType
	if err := database.DB.Find(&productTypes).Error; err != nil {
		log.Println("Error retrieving product types:", err)
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(productTypes)
}

func CreateProductType(w http.ResponseWriter, r *http.Request) {
	var productType models.ProductType
	if err := json.NewDecoder(r.Body).Decode(&productType); err != nil {
		http.Error(w, "Invalid input", http.StatusBadRequest)
		return
	}
	database.DB.Create(&productType)
	json.NewEncoder(w).Encode(productType)
}

func DeleteProductType(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	var productType models.ProductType
	if err := database.DB.First(&productType, params["id"]).Error; err != nil {
		http.Error(w, "Product type not found", http.StatusNotFound)
		return
	}

	// delete the products associated with the product type
	database.DB.Model(&productType).Association("Products").Clear()

	database.DB.Delete(&productType)
	json.NewEncoder(w).Encode(productType)
}