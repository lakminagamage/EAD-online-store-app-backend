package com.ead.order_service.service;
import com.ead.order_service.dto.OrderDTO;

import java.util.List;
public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);
    void deleteOrder(Long orderId);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getAllOrders();
    void updatePaymentStatus(Long orderId, String status);
    List<OrderDTO> getOrdersByUserId(Long userId);
}
