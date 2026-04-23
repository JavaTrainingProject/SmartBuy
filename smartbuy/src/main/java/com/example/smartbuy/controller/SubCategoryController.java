package com.example.smartbuy.controller;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.service.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

        private final SubCategoryService service;

        public SubCategoryController(SubCategoryService service) {
            this.service = service;
        }

        @PostMapping
        public ResponseEntity<SubCategoryResponseDto> create(
                @RequestBody SubCategoryRequestDto dto) {

            return ResponseEntity.ok(service.createSubCategory(dto));
        }
    }