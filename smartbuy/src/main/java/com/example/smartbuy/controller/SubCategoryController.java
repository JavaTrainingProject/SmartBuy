package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.dtos.UpdateStatusRequestDto;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
public class SubCategoryController {

        private final SubCategoryService subCategoryService;

        public SubCategoryController(SubCategoryService service) {
            this.subCategoryService = service;
        }

        @PostMapping
       @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<SubCategoryResponseDto> create(
                @RequestBody SubCategoryRequestDto dto) {

            return ResponseEntity.ok(subCategoryService.createSubCategory(dto));
        }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<SubCategoryResponseDto> getSubCategoryById(@PathVariable Long id) {
        SubCategoryResponseDto dto = subCategoryService.getSubCategoryById(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<?>> getActiveSubCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                subCategoryService.getAllActiveSubCategories(page, size)
        );
    }

    @GetMapping("/categories/{categoryId}/subcategories")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<SubCategoryResponseDto>>> getActiveSubCategories(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ApiResponse<List<SubCategoryResponseDto>> response =
                subCategoryService.getActiveSubCategoriesByCategory(categoryId, page, size);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequestDto requestDto) {

        ApiResponse<String> response =
                subCategoryService.updateSubCategoryStatus(id, requestDto.getStatus());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SubCategoryResponseDto>> updateSubCategory(
            @PathVariable Long id,
            @Valid @RequestBody SubCategoryRequestDto dto) {

        SubCategoryResponseDto response =
                subCategoryService.updateSubCategory(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "SubCategory updated successfully", response)
        );
    }


    @GetMapping
    public ApiResponse<Page<SubCategoryResponseDto>> getAllSubCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {

        return subCategoryService.getAllSubCategories(
                page,
                size,
                sortBy
        );
    }


    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<SubCategoryResponseDto>>> getByStatus(
            @RequestParam Status status) {

        ApiResponse<List<SubCategoryResponseDto>> response =
                subCategoryService.getByStatus(status);

        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(
            @PathVariable Long id) {

        subCategoryService.softDeleteSubCategory(id);

        return ResponseEntity.ok(
                "SubCategory deactivated successfully");
    }
    }