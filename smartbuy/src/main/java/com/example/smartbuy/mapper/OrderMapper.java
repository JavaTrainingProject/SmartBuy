package com.example.smartbuy.mapper;

import com.example.smartbuy.entity.Order;

public class OrderMapper {

    public static Order createOrder(Long userId) {

        Order order = new Order();
        order.setUserId(userId);

        return order;
    }
}