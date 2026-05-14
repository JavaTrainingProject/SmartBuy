package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.OrderResponseDto;
import com.example.smartbuy.entity.Order;

public class OrderMapper {

    public static OrderResponseDto toDto(Order order) {

        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus().name());
        dto.setItems(order.getItems());

        return dto;
    }
}