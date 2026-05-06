package com.example.smartbuy.service;

import com.example.smartbuy.entity.Order;
import com.example.smartbuy.response.ApiResponse;

import java.util.List;

public interface OrderService {

    void placeOrder(Long userId);

    ApiResponse<List<Order>> getOrders(Long userId);
}