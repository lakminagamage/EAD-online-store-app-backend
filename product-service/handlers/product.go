package handlers

import (
	"encoding/json"
	"log"
	"net/http"
	"product-service/models"
	"product-service/pkg/database"
	"strconv"
	"strings"

	"github.com/gorilla/mux"
	"gorm.io/gorm"
)

func paginate(r *http.Request, query *gorm.DB) *gorm.DB {
	page, _ := strconv.Atoi(r.URL.Query().Get("page"))
	if page < 1 {
		page = 1
	}
	pageSize, _ := strconv.Atoi(r.URL.Query().Get("page_size"))
	if pageSize < 1 {
		pageSize = 10
	}
	offset := (page - 1) * pageSize
	return query.Offset(offset).Limit(pageSize)
}

func GetAllProducts(w http.ResponseWriter, r *http.Request) {
    var products []models.Product
    query := paginate(r, database.DB)
    if err := query.Find(&products).Error; err != nil {
        log.Println("Error retrieving products:", err)
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    for i, product := range products {
        var productType models.ProductType
        if err := database.DB.First(&productType, product.ProductTypeID).Error; err != nil {
            log.Println("Error retrieving product type:", err)
            http.Error(w, err.Error(), http.StatusInternalServerError)
            return
        }
        products[i].ProductType = productType
    }
    
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

    var productType models.ProductType
    if err := database.DB.First(&productType, product.ProductTypeID).Error; err != nil {
        http.Error(w, "Product type not found", http.StatusNotFound)
        return
    }

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(product)
}

func GetAllProductsByProductType(w http.ResponseWriter, r *http.Request) {
    params := mux.Vars(r)
    var productType models.ProductType
    if err := database.DB.First(&productType, params["id"]).Error; err != nil {
        http.Error(w, "Product type not found", http.StatusNotFound)
        return
    }

    var products []models.Product
    query := database.DB.Where("product_type_id = ?", productType.ID)
    query = paginate(r, query)

    if err := query.Find(&products).Error; err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(products)
}

func GetProductsByIDs(w http.ResponseWriter, r *http.Request) {
    var requestBody struct {
        IDs []string `json:"product_ids"`
    }
    if err := json.NewDecoder(r.Body).Decode(&requestBody); err != nil {
        http.Error(w, "Invalid input", http.StatusBadRequest)
        return
    }

    log.Println("Product IDs:", requestBody.IDs)

    var products []models.Product
    if err := database.DB.Where("id IN ?", requestBody.IDs).Find(&products).Error; err != nil {
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(products)
}

func CreateProduct(w http.ResponseWriter, r *http.Request) {
    var product models.Product
    if err := json.NewDecoder(r.Body).Decode(&product); err != nil {
        http.Error(w, "Invalid input", http.StatusBadRequest)
        return
    }

    // check the product type is exists
    var productType models.ProductType
    if err := database.DB.First(&productType, product.ProductTypeID).Error; err != nil {
        http.Error(w, "Product type not found", http.StatusNotFound)
        return
    }

    if product.Stock == 0 {
        product.Stock = 0
    }

    database.DB.Create(&product)

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(product)
}

func UpdateStock(w http.ResponseWriter, r *http.Request) {
    params := mux.Vars(r)
    var product models.Product
    if err := database.DB.First(&product, params["id"]).Error; err != nil {
        http.Error(w, "Product not found", http.StatusNotFound)
        return
    }

    new_stock := r.URL.Query().Get("stock")
    if new_stock == "" {
        http.Error(w, "Stock is required", http.StatusBadRequest)
        return
    }
    
    new_stock_int, err := strconv.Atoi(new_stock)
    if err != nil {
        http.Error(w, "Invalid stock", http.StatusBadRequest)
        return
    }

    product.Stock = new_stock_int
    database.DB.Save(&product)

    w.Header().Set("Content-Type", "application/json")
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

    // check the product type is exists
    var productType models.ProductType
    if err := database.DB.First(&productType, product.ProductTypeID).Error; err != nil {
        http.Error(w, "Product type not found", http.StatusNotFound)
        return
    }

    database.DB.Save(&product)

    w.Header().Set("Content-Type", "application/json")
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

func SearchProducts(w http.ResponseWriter, r *http.Request) {
    var products []models.Product
    query := database.DB

    if name := r.URL.Query().Get("name"); name != "" {
        query = query.Where("LOWER(name) LIKE ?", "%"+strings.ToLower(name)+"%")
    }

    minPrice := r.URL.Query().Get("min_price")
    maxPrice := r.URL.Query().Get("max_price")

    if minPrice != "" && maxPrice != "" {
        query = query.Where("price BETWEEN ? AND ?", minPrice, maxPrice)
    } else if minPrice != "" {
        query = query.Where("price >= ?", minPrice)
    } else if maxPrice != "" {
        query = query.Where("price <= ?", maxPrice)
    }

    if inStock := r.URL.Query().Get("in_stock"); inStock != "" && inStock == "true" {
        query = query.Where("stock > 0")
    }

    if productTypeID := r.URL.Query().Get("product_type_id"); productTypeID != "" {
        query = query.Where("product_type_id = ?", productTypeID)
    }

    query = paginate(r, query)
    if err := query.Find(&products).Error; err != nil {
        log.Println("Error retrieving products:", err)
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
    }

    // Uncomment this block to include the product type in the response
    // for i, product := range products {
    //     var productType models.ProductType
    //     if err := database.DB.First(&productType, product.ProductTypeID).Error; err != nil {
    //         log.Println("Error retrieving product type:", err)
    //         http.Error(w, err.Error(), http.StatusInternalServerError)
    //         return
    //     }
    //     products[i].ProductType = productType
    // }

    w.Header().Set("Content-Type", "application/json")
    json.NewEncoder(w).Encode(products)
}
