package com.example.smartbuy.controller;

import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<String> placeOrder(@PathVariable Long userId) {

        orderService.placeOrder(userId);

        return new ApiResponse<>("SUCCESS", "Order placed successfully", null);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<?> getOrders(@PathVariable Long userId) {
        return orderService.getOrders(userId);
    }
}