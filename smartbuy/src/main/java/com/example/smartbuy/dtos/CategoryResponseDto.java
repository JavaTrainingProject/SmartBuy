package com.example.smartbuy.dtos;

import com.example.smartbuy.enums.Status;

import java.time.LocalDateTime;

public class CategoryResponseDto {

    private Long id;
    private String categoryName;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryResponseDto() {
    }
    public CategoryResponseDto(Long id,
                               String categoryName,
                               Status status,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}