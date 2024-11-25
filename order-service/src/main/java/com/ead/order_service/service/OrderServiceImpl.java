package com.ead.order_service.service;
import com.ead.order_service.dto.OrderDTO;
import com.ead.order_service.dto.OrderItemDTO;
import com.ead.order_service.model.Order;
import com.ead.order_service.model.OrderItem;
import com.ead.order_service.repository.OrderRepository;
import com.ead.order_service.exception.OrderNotFoundException;
import com.ead.order_service.exception.RequestFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.util.UriComponentsBuilder;
import com.ead.order_service.helper.RequestHelper;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    private void updateProductStock(Long productId, int quantity) throws RequestFailedException {
        String urlString = UriComponentsBuilder.fromHttpUrl(apiGatewayUrl + "/products/" + productId + "/stock")
            .queryParam("request_quantity", quantity)
            .toUriString();

        logger.info("Requesting URL: {} to update stock for product id: {}", urlString, productId);

        int responseCode = RequestHelper.SendPatchRequest(urlString, "");

        if (responseCode != HttpStatus.OK.value() && responseCode != HttpStatus.NO_CONTENT.value()) {
            throw new RuntimeException("Failed to update stock for product id: " + productId);
        }
    }

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        logger.info("Creating order for userId: {}", orderDTO.getUserId());

        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setStatus(orderDTO.getStatus() != null ? orderDTO.getStatus() : "PENDING");

        // validate user
        try {
            String urlString = UriComponentsBuilder.fromHttpUrl(apiGatewayUrl + "/users/" + orderDTO.getUserId())
                    .toUriString();

            logger.info("Requesting URL: {} to validate user id: {}", urlString, orderDTO.getUserId());

            CloseableHttpResponse response = RequestHelper.SendGetRequest(urlString);

            if (response.getCode() != HttpStatus.OK.value()) {
                throw new RuntimeException("User not found with id: " + orderDTO.getUserId());
            }
        } catch (Exception e) {
            logger.error("Failed to validate user", e);
            throw new RuntimeException("Order creation failed due to user validation failure");
        }

        List<OrderItem> orderItems = orderDTO.getItems().stream().map(itemDTO -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDTO.getProductId());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(itemDTO.getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        List<Long> updatedProducts = new ArrayList<>();

        // validate the products and product stocks update the stocks of the products
        try {
            for (OrderItem item : orderItems) {
                updateProductStock(item.getProductId(), item.getQuantity());

                updatedProducts.add(item.getProductId());
            }
        } catch (Exception e) {
            logger.error("Failed to update stock, rolling back order creation", e);
            orderRepository.delete(savedOrder);

            // undo the product stock update, if failed one of the stock update
            try {
                for (OrderItem item : orderItems) {
                    if (updatedProducts.contains(item.getProductId())) {
                        // update the stock to its previous value
                        updateProductStock(item.getProductId(), -item.getQuantity());
                    }
                }
            } catch (Exception ex) {
                logger.error("Failed to undo stock update", ex);
                throw new RuntimeException("Order creation failed due to stock update failure, Failed to undo stock update");
            }

            throw new RuntimeException("Order creation failed due to stock update failure");
        }

        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setUserId(savedOrder.getUserId());
        responseDTO.setStatus(savedOrder.getStatus());
        responseDTO.setItems(savedOrder.getOrderItems().stream().map(orderItem -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(orderItem.getProductId());
            itemDTO.setQuantity(orderItem.getQuantity());
            itemDTO.setPrice(orderItem.getPrice());
            return itemDTO;
        }).collect(Collectors.toList()));

        logger.info("Order created successfully with id: {}", savedOrder.getId());
        return responseDTO;
    }

    @Override
    public List<OrderDTO> getAllOrders(){
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(order -> {
                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setId(order.getId());
                    orderDTO.setUserId(order.getUserId());
                    orderDTO.setStatus(order.getStatus());

                    List<OrderItemDTO> items = order.getOrderItems().stream().map(orderItem -> {
                        OrderItemDTO itemDTO = new OrderItemDTO();
                        itemDTO.setProductId(orderItem.getProductId());
                        itemDTO.setQuantity(orderItem.getQuantity());
                        itemDTO.setPrice(orderItem.getPrice());
                        return itemDTO;
                    }).collect(Collectors.toList());

                    orderDTO.setItems(items);
                    return orderDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById (Long orderId){
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setStatus(order.getStatus());

        List<OrderItemDTO> items = order.getOrderItems().stream().map(orderItem -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(orderItem.getProductId());
            itemDTO.setQuantity(orderItem.getQuantity());
            itemDTO.setPrice(orderItem.getPrice());
            return itemDTO;
        }).collect(Collectors.toList());

        orderDTO.setItems(items);
        return orderDTO;
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException(orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public void updatePaymentStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        orderRepository.save(order);
        logger.info("Updated payment status for order id: {} to status: {}", orderId, status);
    }
}
