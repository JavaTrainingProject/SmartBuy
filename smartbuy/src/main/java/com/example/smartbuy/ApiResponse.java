package com.example.smartbuy;

public class ApiResponse<T> {
    private String status;
    private boolean message;
    private T data;

    public ApiResponse() {

    }

    public ApiResponse(String status, boolean message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
