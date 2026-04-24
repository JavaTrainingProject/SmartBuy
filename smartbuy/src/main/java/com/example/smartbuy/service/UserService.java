package com.example.smartbuy.service;

import com.example.smartbuy.dtos.*;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);

    UserResponseDto registerAdmin(UserRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    LoginResponseDto refreshToken(RefreshRequestDto dto);
}
