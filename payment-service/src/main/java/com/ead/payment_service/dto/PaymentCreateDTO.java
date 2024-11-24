package com.ead.payment_service.dto;

import lombok.Data;

@Data
public class PaymentCreateDTO {
    private String paymentType;
    private Long orderId;
}
