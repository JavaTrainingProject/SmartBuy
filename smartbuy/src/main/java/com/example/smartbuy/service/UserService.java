package com.example.smartbuy.service;

import com.example.smartbuy.dtos.*;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);

    UserResponseDto registerAdmin(UserRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    LoginResponseDto refreshToken(RefreshRequestDto dto);

    MessageResponseDto logout(HttpServletRequest request);
}
