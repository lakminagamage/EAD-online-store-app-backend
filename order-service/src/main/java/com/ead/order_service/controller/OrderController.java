package com.ead.order_service.controller;

import com.ead.order_service.dto.OrderDTO;
import com.ead.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
public class OrderController extends AbstractController{

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return createdResponse(createdOrder);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return noContentResponse();
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

}
