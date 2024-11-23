package models

import "gorm.io/gorm"

type Product struct {
    gorm.Model
    Name string `json:"name"`
    Description string `json:"description"`
    Price float64 `json:"price"`
    Stock int `json:"stock"`

    ProductTypeID uint `json:"product_type_id"`
    ProductType ProductType `json:"product_type"`
    
    Images []ProductImage `json:"images"`
}