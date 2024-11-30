package models

import "gorm.io/gorm"

type ProductImage struct {
    gorm.Model
    URL string `json:"url"`
    ProductID uint `json:"product_id"`
}
