package database

import (
	"fmt"
	"log"
	"os"
	"time"

	"github.com/joho/godotenv"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

var DB *gorm.DB

func Connect() {
    // Load .env file
    err_env := godotenv.Load()
    if err_env != nil {
        log.Fatalf("Error loading .env file")
    }

    dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=require",
        os.Getenv("DB_HOST"),
        os.Getenv("DB_USER"),
        os.Getenv("DB_PASSWORD"),
        os.Getenv("DB_NAME"),
        os.Getenv("DB_PORT"),
    )

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

