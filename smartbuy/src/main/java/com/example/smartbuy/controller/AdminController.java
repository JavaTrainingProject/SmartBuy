package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.UserRequestDto;
import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.service.UserService;
import com.example.smartbuy.serviceImpls.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService){
        this.userService=userService;
    }


    @PostMapping("/register-admin")
    public UserResponseDto registerAdmin(@Valid @RequestBody UserRequestDto dto){
        return userService.registerAdmin(dto);
    }
}
