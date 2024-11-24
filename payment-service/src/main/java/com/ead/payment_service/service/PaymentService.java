package com.ead.payment_service.service;

import com.ead.payment_service.dto.PaymentCreateDTO;
import com.ead.payment_service.dto.PaymentUpdateDTO;
import com.ead.payment_service.dto.PaymentDTO;
import com.ead.payment_service.model.Payment;
import com.ead.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PaymentDTO createPayment(PaymentCreateDTO paymentCreateDTO) {
        Payment payment = new Payment();
        payment.setPaymentType(paymentCreateDTO.getPaymentType());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);
        return mapToPaymentDTO(payment);
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return mapToPaymentDTO(payment);
    }

    public PaymentDTO updatePayment(PaymentUpdateDTO paymentUpdateDTO) {
        Payment payment = paymentRepository.findById(paymentUpdateDTO.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setPaymentType(paymentUpdateDTO.getPaymentType());
        payment.setUpdatedAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);
        return mapToPaymentDTO(payment);
    }

    public void deletePaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        paymentRepository.delete(payment);
    }

    private PaymentDTO mapToPaymentDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setPaymentType(payment.getPaymentType());
        paymentDTO.setCreatedAt(payment.getCreatedAt().format(formatter));
        paymentDTO.setUpdatedAt(payment.getUpdatedAt().format(formatter));
        return paymentDTO;
    }
}