package com.example.smartbuy.dtos;

import com.example.smartbuy.enums.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequestDto {

    @NotNull(message = "Status is required")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
