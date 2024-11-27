package com.ead.payment_service.service;

import com.ead.payment_service.dto.PaymentCreateDTO;
import com.ead.payment_service.dto.PaymentUpdateDTO;
import com.ead.payment_service.dto.PaymentDTO;
import com.ead.payment_service.model.Payment;
import com.ead.payment_service.repository.PaymentRepository;
import com.ead.payment_service.helper.RequestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.springframework.http.HttpStatus;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private CloseableHttpResponse getOrderByID(Long orderId) {
        String urlString = UriComponentsBuilder.fromHttpUrl(apiGatewayUrl + "/orders/" + orderId)
                .toUriString();

        try (CloseableHttpResponse response = RequestHelper.SendGetRequest(urlString)) {
            if (response.getCode() != HttpStatus.OK.value()) {
                throw new RuntimeException("Order not found with id: " + orderId);
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Payment creation failed due to order validation failure");
        }
    }

    private boolean isOrderValid(CloseableHttpResponse order) {
        return order.getCode() == HttpStatus.OK.value();
    }

    public PaymentDTO createPayment(PaymentCreateDTO paymentCreateDTO) {
        // validate order
        CloseableHttpResponse order = getOrderByID(paymentCreateDTO.getOrderId());
        if (!isOrderValid(order)) {
            throw new RuntimeException("Payment creation failed due to order validation failure");
        }
        
        Payment payment = new Payment();
        payment.setPaymentType(paymentCreateDTO.getPaymentType());
        payment.setOrderId(paymentCreateDTO.getOrderId());
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
        // validate order
        CloseableHttpResponse order = getOrderByID(paymentUpdateDTO.getOrderId());
        if (!isOrderValid(order)) {
            throw new RuntimeException("Payment update failed due to order validation failure");
        }

        Payment payment = paymentRepository.findById(paymentUpdateDTO.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        payment.setPaymentType(paymentUpdateDTO.getPaymentType());
        payment.setOrderId(paymentUpdateDTO.getOrderId());
        payment.setUpdatedAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);
        return mapToPaymentDTO(payment);
    }

    public List<PaymentDTO> getPaymentsByOrderId(Long orderId) {
        // validate order
        CloseableHttpResponse order = getOrderByID(orderId);
        if (!isOrderValid(order)) {
            throw new RuntimeException("Payments not found due to order validation failure");
        }
        
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        if (payments.isEmpty()) {
            throw new ResourceNotFoundException("Payments not found");
        }

        if (payments.size() > 1) {
            throw new RuntimeException("Multiple payments found for order id: " + orderId);
        }

        return payments.stream().map(this::mapToPaymentDTO).collect(Collectors.toList());
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
        paymentDTO.setOrderId(payment.getOrderId());
        paymentDTO.setCreatedAt(payment.getCreatedAt().format(formatter));
        paymentDTO.setUpdatedAt(payment.getUpdatedAt().format(formatter));
        return paymentDTO;
    }
}