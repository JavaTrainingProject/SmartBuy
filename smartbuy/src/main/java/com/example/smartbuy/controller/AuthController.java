package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.*;
import com.example.smartbuy.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/user")
    public UserResponseDto registerUser(@Valid @RequestBody UserRequestDto dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto) {

        return userService.login(dto);
    }


    @PostMapping("/refreshToken")
    public LoginResponseDto refreshToken(@Valid @RequestBody RefreshRequestDto dto) {
        return userService.refreshToken(dto);
    }

    @PostMapping("/logout")
    public MessageResponseDto logout(HttpServletRequest request){
        return userService.logout(request);
    }


@PostMapping("/verify")
public ResponseEntity<String> verifyOtp(
        @RequestParam String email,
        @RequestParam String otp) {

    return ResponseEntity.ok(userService.verifyOtp(email, otp));
}

@PostMapping("/resend")
public ResponseEntity<String> resendOtp(@RequestParam String email) {
    return ResponseEntity.ok(userService.resendOtp(email));
}
}