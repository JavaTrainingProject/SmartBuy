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

/*
 private String message;

    private boolean success;

    private T data;

    // Constructor

    public ApiResponse(String message, boolean success, T data) {

        this.message = message;

        this.success = success;

        this.data = data;

    }

    // Getter & Setter

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public boolean isSuccess() {return success;}

    public void setSuccess(boolean success) {this.success = success;}

    public T getData() {return data;}

    public void setData(T data) {this.data = data;}

}l
 */