package com.ead.order_service.controller;

import com.ead.order_service.dto.OrderDTO;
import com.ead.order_service.dto.OrderItemDTO;
import com.ead.order_service.model.Order;
import com.ead.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/orders")
public class OrderController extends AbstractController{

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        System.out.println("OrderDTO: " + orderDTO);
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return createdResponse(createdOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> deleteOrder(@PathVariable Long orderId) {
        List<OrderItemDTO> orderItems = orderService.deleteOrder(orderId);

        return successResponse(orderItems, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return successResponse(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return successResponse(orders, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/payment-status")
    public ResponseEntity<Void> updatePaymentStatus(@PathVariable Long orderId, @RequestParam String status) {
        orderService.updatePaymentStatus(orderId, status);
        return noContentResponse();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return successResponse(orders, HttpStatus.OK);
    }

}
