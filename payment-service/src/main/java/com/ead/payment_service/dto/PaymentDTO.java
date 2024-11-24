package com.ead.payment_service.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private Long paymentId;
    private String paymentType;
    private String createdAt;
    private String updatedAt;
}
