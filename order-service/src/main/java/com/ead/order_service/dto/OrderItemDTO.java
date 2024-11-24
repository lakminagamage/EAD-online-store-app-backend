package com.ead.order_service.dto;

import lombok.Data;

@Data

public class OrderItemDTO {
    private Long productId;
    private int quantity;
    private double price;
}
