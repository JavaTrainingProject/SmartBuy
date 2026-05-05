package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.UserResponseDto;
import com.example.smartbuy.dtos.UserUpdateRequestDto;
import com.example.smartbuy.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


    @RestController
    @RequestMapping("/users")
    public class UserController {

        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }


        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public Page<UserResponseDto> getAllUsers(Pageable pageable) {
            return userService.getAllUsers(pageable);
        }


        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public UserResponseDto getUserById(@PathVariable Long id) {
            return userService.getUserById(id);
        }


        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public UserResponseDto updateUser(
                @PathVariable Long id,
                @RequestBody UserUpdateRequestDto dto) {
            return userService.updateUser(id, dto);
        }


        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public String deleteUser(@PathVariable Long id) {
            userService.deleteUser(id);
            return "User deleted successfully";
        }
    }

