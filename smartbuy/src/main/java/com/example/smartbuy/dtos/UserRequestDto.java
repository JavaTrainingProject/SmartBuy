package com.example.smartbuy.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be 3-50 characters")
    private String user_name;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String user_email;
    @NotBlank(message="Password is required")
    @Size(min=6, message="Password must be atleast 6 characters")
    private String user_password;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
