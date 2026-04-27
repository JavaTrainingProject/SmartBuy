package com.example.smartbuy.service;

import com.example.smartbuy.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);

    UserResponseDto registerAdmin(UserRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    LoginResponseDto refreshToken(RefreshRequestDto dto);


    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto dto);

    void deleteUser(Long id);

}
