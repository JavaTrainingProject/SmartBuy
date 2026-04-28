package com.example.smartbuy;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String status;
    private String message;
    private int statusCode;
    private LocalDateTime timestamp;

    public ErrorResponse(String status, String message, int statusCode) {
        this.status = status;
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getStatusCode() { return statusCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
