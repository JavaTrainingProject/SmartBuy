package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.OrderRequestDto;
import com.example.smartbuy.dtos.OrderResponseDto;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto request) {
        return new ApiResponse<>("SUCCESS",
                "Order placed successfully",
                orderService.placeOrder(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<OrderResponseDto>> getOrders() {
        return orderService.getOrders();
    }
}