package com.example.smartbuy.service;

import com.example.smartbuy.dtos.OrderRequestDto;
import com.example.smartbuy.dtos.OrderResponseDto;
import com.example.smartbuy.response.ApiResponse;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto request);

    ApiResponse<List<OrderResponseDto>> getOrders();
}