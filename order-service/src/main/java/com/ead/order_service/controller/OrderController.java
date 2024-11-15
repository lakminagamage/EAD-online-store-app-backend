package com.ead.order_service.controller;

import com.ead.order_service.dtos.CreateOrderDto;
import com.ead.order_service.dtos.GetOrderDto;
import com.ead.order_service.dtos.UpdateOrderDto;
import com.ead.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestContoller
@RequestMapping("/api/v1/orders")
public class OrderController extends AbstractController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<GetOrderDto>> getAllOrders() {
        return successResponse(orderService.getAllOrders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetOrderDto> getOrderById(@PathVariable Long id) {
        return successResponse(orderService.getOrderById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GetOrderDto> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        return createdResponse(orderService.createOrder(createOrderDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetOrderDto> updateOrder(
            @PathVariable Long id,
            @RequestBody UpdateOrderDto updateOrderDto
    ) {
        return successResponse(orderService.updateOrder(id, updateOrderDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return noContentResponse();
    }
}
