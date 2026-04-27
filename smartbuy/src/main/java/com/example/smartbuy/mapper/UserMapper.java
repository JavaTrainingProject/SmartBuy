package com.example.smartbuy.mapper;

import com.example.smartbuy.dtos.UserRequestDto;
import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public static UserEntity toEntity(UserRequestDto dto) {
        UserEntity user = new UserEntity();
        user.setUser_name(dto.getUser_name());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getUser_password());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    public UserResponseDto toDto(UserEntity user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUser_name(user.getUser_name());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

}
