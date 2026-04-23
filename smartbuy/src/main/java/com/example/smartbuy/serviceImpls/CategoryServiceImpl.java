package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.exception.CategoryAlreadyExistsException;
import com.example.smartbuy.mapper.CategoryMapper;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.service.CategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = new CategoryMapper();
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto dto) {

        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }

        CategoryEntity category = categoryMapper.toEntity(dto);

        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category.setStatus(Status.ACTIVE);

        CategoryEntity saved = categoryRepository.save(category);

        return CategoryMapper.toDto(saved);
    }


}