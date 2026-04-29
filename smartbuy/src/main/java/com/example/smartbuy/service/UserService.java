package com.example.smartbuy.service;

import com.example.smartbuy.dtos.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto dto);

    UserResponseDto registerAdmin(UserRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    LoginResponseDto refreshToken(RefreshRequestDto dto);

    MessageResponseDto logout(HttpServletRequest request);

    String verifyOtp(String email, String otp);
    String resendOtp(String email);

    Page<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto dto);

    void deleteUser(Long id);
}
