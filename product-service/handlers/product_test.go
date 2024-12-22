package handlers_test

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"product-service/handlers"
	"product-service/models"
	"product-service/pkg/database"
	"strings"
	"testing"

	"github.com/glebarez/sqlite"
	"gorm.io/gorm"
)

func setupTestDB() *gorm.DB {
	db, err := gorm.Open(sqlite.Open(":memory:"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}
	db.AutoMigrate(&models.Product{}, &models.ProductImage{})

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
		ID           uint   `json:"ID"`
		Name         string `json:"name"`
		ProductType  string `json:"product_type"`
		Stock        int    `json:"stock"`
	}

	var response struct {
		Total int               `json:"total"`
		Data  []ProductResponse `json:"data"`
	}

	if err := json.Unmarshal(rr.Body.Bytes(), &response); err != nil {
		t.Fatalf("could not unmarshal response: %v", err)
	}

	actual := response.Data

	// Print the actual response
	t.Logf("actual response: %v", actual)

	expected := []ProductResponse{
		{ID: 1, Name: "Product1", ProductType: "Type1", Stock: 10},
		{ID: 2, Name: "Product2", ProductType: "Type1", Stock: 20},
		{ID: 3, Name: "Product3", ProductType: "Type1", Stock: 30},
		{ID: 4, Name: "Product4", ProductType: "Type2", Stock: 40},
		{ID: 5, Name: "Product5", ProductType: "Type2", Stock: 50},
		{ID: 6, Name: "Product6", ProductType: "Type2", Stock: 60},
		{ID: 7, Name: "Product7", ProductType: "Type3", Stock: 70},
		{ID: 8, Name: "Product8", ProductType: "Type3", Stock: 80},
		{ID: 9, Name: "Product9", ProductType: "Type3", Stock: 90},
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

func TestGetProductByID(t *testing.T) {
	db := setupTestDB()
	database.DB = db

	req, err := http.NewRequest("GET", "/products/1", nil)
	if err != nil {
		t.Fatal(err)
	}

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(handlers.GetProductByID)

	handler.ServeHTTP(rr, req)

	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler returned wrong status code: got %v want %v", status, http.StatusOK)
	}

	var actual models.Product
	if err := json.Unmarshal(rr.Body.Bytes(), &actual); err != nil {
		t.Fatalf("could not unmarshal response: %v", err)
	}

	expected := models.Product{Name: "Product1", ProductTypeID: 1, Stock: 10}
	if actual.Name != expected.Name || actual.ProductTypeID != expected.ProductTypeID || actual.Stock != expected.Stock {
		t.Errorf("handler returned unexpected product: got %v want %v", actual, expected)
	}
}

func TestUpdateStock(t *testing.T) {
	db := setupTestDB()
	database.DB = db

	req, err := http.NewRequest("PUT", "/products/1/stock", strings.NewReader(`{"stock": 100}`))
	if err != nil {
		t.Fatal(err)
	}
	req.Header.Set("Content-Type", "application/json")

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(handlers.UpdateStock)

	handler.ServeHTTP(rr, req)

	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler returned wrong status code: got %v want %v", status, http.StatusOK)
	}

	var actual models.Product
	if err := json.Unmarshal(rr.Body.Bytes(), &actual); err != nil {
		t.Fatalf("could not unmarshal response: %v", err)
	}

	expected := models.Product{Name: "Product1", ProductTypeID: 1, Stock: 100}
	if actual.Name != expected.Name || actual.ProductTypeID != expected.ProductTypeID || actual.Stock != expected.Stock {
		t.Errorf("handler returned unexpected product: got %v want %v", actual, expected)
	}
}

func TestCreateProduct(t *testing.T) {
	db := setupTestDB()
	database.DB = db

	newProduct := models.Product{Name: "NewProduct", ProductTypeID: 1, Stock: 10}
	body, err := json.Marshal(newProduct)
	if err != nil {
		t.Fatal(err)
	}

	req, err := http.NewRequest("POST", "/products", strings.NewReader(string(body)))
	if err != nil {
		t.Fatal(err)
	}

	rr := httptest.NewRecorder()
	handler := http.HandlerFunc(handlers.CreateProduct)

	handler.ServeHTTP(rr, req)

	if status := rr.Code; status != http.StatusOK {
		t.Errorf("handler returned wrong status code: got %v want %v", status, http.StatusOK)
	}

	var actual models.Product
	if err := json.Unmarshal(rr.Body.Bytes(), &actual); err != nil {
		t.Fatalf("could not unmarshal response: %v", err)
	}

	if actual.Name != newProduct.Name || actual.ProductTypeID != newProduct.ProductTypeID || actual.Stock != newProduct.Stock {
		t.Errorf("handler returned unexpected product: got %v want %v", actual, newProduct)
	}
}