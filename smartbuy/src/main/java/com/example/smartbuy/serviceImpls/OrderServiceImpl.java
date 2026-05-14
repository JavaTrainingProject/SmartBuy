package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.OrderRequestDto;
import com.example.smartbuy.dtos.OrderResponseDto;
import com.example.smartbuy.entity.*;
import com.example.smartbuy.enums.OrderStatus;
import com.example.smartbuy.mapper.OrderMapper;
import com.example.smartbuy.repository.*;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CartRepository cartRepository,
                            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }


    @Override
    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto request) {

        Long userId = getCurrentUserId();

        List<CartProduct> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }


        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setAddress(request.getAddress());

        List<OrderProduct> items = new ArrayList<>();

        for (CartProduct cart : cartItems) {

            OrderProduct op = new OrderProduct();
            op.setProductId(cart.getProductId());
            op.setProductName(cart.getProductName());
            op.setPrice(cart.getPrice());
            op.setQuantity(cart.getQuantity());
            op.setImageUrl(cart.getImageUrl());


            op.setOrder(order);

            items.add(op);
        }

        order.setItems(items);

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        order.setTotalAmount(total);


        Order savedOrder = orderRepository.saveAndFlush(order);


        cartRepository.deleteAll(cartItems);


        return OrderMapper.toDto(savedOrder);
    }


    @Override
    public ApiResponse<List<OrderResponseDto>> getOrders() {

        Long userId = getCurrentUserId();

        List<OrderResponseDto> list = orderRepository.findByUserId(userId)
                .stream()
                .map(OrderMapper::toDto)
                .toList();

        return new ApiResponse<>("SUCCESS", "Orders fetched", list);
    }
}