package com.app.ecom.service;

import com.app.ecom.dto.OrderItemResponse;
import com.app.ecom.dto.OrderResponse;
import com.app.ecom.enums.OrderStatus;
import com.app.ecom.exception.NotFoundException;
import com.app.ecom.model.*;
import com.app.ecom.repository.OrderRepository;
import com.app.ecom.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final UserRepo userRepo;

    public OrderServiceImpl(OrderRepository orderRepository, CartItemService cartItemService, UserRepo userRepo){
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
        this.userRepo = userRepo;
    }

    @Override
    public OrderResponse createOrder(String userId) {

        // Validate for user
        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
        if(userOpt.isEmpty()){throw new NotFoundException("User not found with Id: "+userId);}
        User user = userOpt.get();

        // Validate for cart items
        List<CartItem> cartItemList = cartItemService.getCartItems(Long.valueOf(userId));
        if(cartItemList.isEmpty()){throw new NotFoundException("There are no cart items available for userId: " + userId);}

        // Calculate total price
        BigDecimal totalPrice = cartItemList.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItemList = cartItemList.stream().map(cartItem -> new OrderItem(cartItem.getProduct(),cartItem.getQuantity(),cartItem.getPrice(),order)).toList();
        order.setItems(orderItemList);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartItemService.clear(userId);

        return mapToOrderResponse(savedOrder);
    }

    private static OrderResponse mapToOrderResponse(Order order){
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getItems().stream().map(orderItem -> new OrderItemResponse(orderItem.getId(),orderItem.getProduct().getId(),orderItem.getQuantity(),orderItem.getPrice(),orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
