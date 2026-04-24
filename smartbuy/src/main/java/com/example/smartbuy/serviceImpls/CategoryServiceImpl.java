package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.ApiResponse;
import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.exception.CategoryAlreadyExistsException;
import com.example.smartbuy.mapper.CategoryMapper;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public ApiResponse<List<CategoryResponseDto>> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<CategoryEntity> categoryPage = categoryRepository.findAll(pageable);

        List<CategoryResponseDto> responseList = categoryPage.getContent()
                .stream()
                .map(CategoryMapper::toDto)
                .toList();

        return new ApiResponse<>(
                "Categories fetched successfully",true,responseList
        );
    }
}