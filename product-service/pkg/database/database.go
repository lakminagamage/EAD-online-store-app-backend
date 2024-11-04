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
    dsn := "host=localhost user=postgres password=Abcd1234 dbname=productdb port=5432 sslmode=disable TimeZone=Asia/Shanghai"

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

