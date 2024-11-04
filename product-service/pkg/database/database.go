package database

import (
	"fmt"
	"log"
	"time"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var DB *gorm.DB

func Connect() {
    dsn := "host=ep-small-morning-a58y2k1s.us-east-2.aws.neon.tech user=ead-order-service_owner password=SMWgZJa2ceR0 dbname=ead-product-service port=5432 sslmode=require TimeZone=Asia/Shanghai"

    var err error
    for i := 0; i < 10; i++ {
        DB, err = gorm.Open(postgres.Open(dsn), &gorm.Config{})
        if err == nil {
            fmt.Println("Database connected successfully")
            return
        }
        log.Printf("Error connecting to database: %v. Retrying in 2 seconds...", err)
        time.Sleep(2 * time.Second)
    }

    log.Fatalf("Failed to connect to database after retries: %v", err)
}

