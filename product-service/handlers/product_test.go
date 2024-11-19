package handlers_test

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"product-service/handlers"
	"product-service/models"
	"product-service/pkg/database"
	"testing"

	"github.com/glebarez/sqlite"
	"gorm.io/gorm"
)

func setupTestDB() *gorm.DB {
	db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	db.AutoMigrate(&models.Product{})

	db.Create(&models.ProductType{Name: "Type1"})
	db.Create(&models.ProductType{Name: "Type2"})
	db.Create(&models.ProductType{Name: "Type3"})

	db.Create(&models.Product{Name: "Product1", ProductTypeID: 1, Stock: 10})
	db.Create(&models.Product{Name: "Product2", ProductTypeID: 1, Stock: 20})
	db.Create(&models.Product{Name: "Product3", ProductTypeID: 1, Stock: 30})
	db.Create(&models.Product{Name: "Product4", ProductTypeID: 2, Stock: 40})
	db.Create(&models.Product{Name: "Product5", ProductTypeID: 2, Stock: 50})
	db.Create(&models.Product{Name: "Product6", ProductTypeID: 2, Stock: 60})
	db.Create(&models.Product{Name: "Product7", ProductTypeID: 3, Stock: 70})
	db.Create(&models.Product{Name: "Product8", ProductTypeID: 3, Stock: 80})
	db.Create(&models.Product{Name: "Product9", ProductTypeID: 3, Stock: 90})

	return db
}

func TestGetAllProducts(t *testing.T) {
	db := setupTestDB()
	database.DB = db

	req, err := http.NewRequest("GET", "/products/", nil)
	if err != nil {
		t.Fatal(err)
	}

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(handlers.GetAllProducts)

	handler.ServeHTTP(rr, req)

	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler returned wrong status code: got %v want %v", status, http.StatusOK)
	}

	type ProductResponse struct {
		ID   uint   `json:"ID"`
		Name string `json:"name"`
	}

	var actual []ProductResponse
	if err := json.Unmarshal(rr.Body.Bytes(), &actual); err != nil {
		t.Fatalf("could not unmarshal response: %v", err)
	}

	expected := []ProductResponse{
		{ID: 1, Name: "Product1"},
		{ID: 2, Name: "Product2"},
		{ID: 3, Name: "Product3"},
		{ID: 4, Name: "Product4"},
		{ID: 5, Name: "Product5"},
		{ID: 6, Name: "Product6"},
		{ID: 7, Name: "Product7"},
		{ID: 8, Name: "Product8"},
		{ID: 9, Name: "Product9"},
	}

	if len(actual) != len(expected) {
		t.Fatalf("handler returned unexpected number of products: got %v want %v", len(actual), len(expected))
	}

	for i, product := range actual {
		if product != expected[i] {
			t.Errorf("handler returned unexpected product at index %d: got %v want %v", i, product, expected[i])
		}
	}
}