package com.example.smartbuy.service;

import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.dtos.UserUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto dto);

    void deleteUser(Long id);


}
