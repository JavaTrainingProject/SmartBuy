package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toDto(UserEntity user) {
        if (user == null) return null;

        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUser_name(user.getUser_name());
        dto.setUser_email(user.getUser_email());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }
}