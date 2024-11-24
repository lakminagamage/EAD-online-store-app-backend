package com.ead.order_service.service;
import com.ead.order_service.dto.OrderDTO;
import com.ead.order_service.dto.OrderItemDTO;
import com.ead.order_service.model.Order;
import com.ead.order_service.model.OrderItem;
import com.ead.order_service.repository.OrderRepository;
import com.ead.order_service.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private static final Logger logger = LogManager.getLogger(OrderServiceImpl.class);

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
    public OrderDTO createOrder(OrderDTO orderDTO) {
        logger.info("Creating order for userId: {}", orderDTO.getUserId());

        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setStatus(orderDTO.getStatus());


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


        OrderDTO responseDTO = new OrderDTO();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setUserId(savedOrder.getUserId());
        responseDTO.setStatus(savedOrder.getStatus() );



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
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException(orderId);
        }
        orderRepository.deleteById(orderId);
    }
}
