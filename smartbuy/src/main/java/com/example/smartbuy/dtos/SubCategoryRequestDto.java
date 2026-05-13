package com.example.smartbuy.dtos;

import com.example.smartbuy.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SubCategoryRequestDto {

    @NotBlank(message = "SubCategory name is required")
    @Size(min = 2, max = 100)
    private String subCategoryName;
    private Status status;
    private Long categoryId;

    public @NotNull(message = "Category ID is required") Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "Category ID is required") Status status) {
        this.status = status;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }


}
