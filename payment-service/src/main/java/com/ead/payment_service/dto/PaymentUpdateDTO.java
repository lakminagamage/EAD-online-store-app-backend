package com.ead.payment_service.dto;

import lombok.Data;

@Data
public class PaymentUpdateDTO {
    private Long paymentId;
    private String paymentType;
}
