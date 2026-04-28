package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.dtos.UpdateStatusRequestDto;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
        public ResponseEntity<SubCategoryResponseDto> create(
                @RequestBody SubCategoryRequestDto dto) {

            return ResponseEntity.ok(subCategoryService.createSubCategory(dto));
        }
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoryResponseDto> getSubCategoryById(@PathVariable Long id) {
        SubCategoryResponseDto dto = subCategoryService.getSubCategoryById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SubCategoryResponseDto>>> getActiveSubCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(subCategoryService.getAllActiveSubCategories(page, size));
    }

    @GetMapping("/categories/{categoryId}/subcategories")
    public ResponseEntity<ApiResponse<List<SubCategoryResponseDto>>> getActiveSubCategories(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ApiResponse<List<SubCategoryResponseDto>> response =
                subCategoryService.getActiveSubCategoriesByCategory(categoryId, page, size);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequestDto requestDto) {

        ApiResponse<String> response =
                subCategoryService.updateSubCategoryStatus(id, requestDto.getStatus());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResponseDto>> updateSubCategory(
            @PathVariable Long id,
            @Valid @RequestBody SubCategoryRequestDto dto) {

        SubCategoryResponseDto response =
                subCategoryService.updateSubCategory(id, dto);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "SubCategory updated successfully", response)
        );
    }

    }