package com.example.smartbuy.dtos;

public class MessageResponseDto {

    public String message;

    public MessageResponseDto() {
    }

    public MessageResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
