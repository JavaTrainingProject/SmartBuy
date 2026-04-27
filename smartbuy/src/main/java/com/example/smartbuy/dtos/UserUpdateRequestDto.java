package com.example.smartbuy.dtos;
import com.example.smartbuy.enums.Status;
import jakarta.validation.constraints.Email;

public class UserUpdateRequestDto {
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String password;

    private Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
