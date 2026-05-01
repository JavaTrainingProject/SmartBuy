package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.dtos.CategoryRequestDto;
import com.example.smartbuy.dtos.CategoryResponseDto;
import com.example.smartbuy.dtos.CategoryWithProductsResponseDto;
import com.example.smartbuy.dtos.ProductResponseDto;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.exception.CategoryAlreadyExistsException;
import com.example.smartbuy.mapper.CategoryMapper;
import com.example.smartbuy.mapper.ProductMapper;
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

        return new ApiResponse<>("success", "Categories fetched successfully",responseList
        );
    }
    @Override
    public ApiResponse<CategoryResponseDto> getCategoryById(Long id) {

        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new ApiResponse<>("success", "Category fetched successfully",
                CategoryMapper.toDto(entity));
    }

    @Override
    public ApiResponse<CategoryResponseDto> updateCategory(Long id, CategoryRequestDto dto) {

        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (dto.getCategoryName() == null || dto.getCategoryName().trim().isEmpty()) {
            throw new RuntimeException("Category name cannot be empty");
        }

        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())
                && !entity.getCategoryName().equalsIgnoreCase(dto.getCategoryName())) {
            throw new RuntimeException("Category name already exists");
        }

        entity.setCategoryName(dto.getCategoryName());
        entity.setCategoryDescription(dto.getCategoryDescription());
        entity.setUpdatedAt(LocalDateTime.now());

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }

        CategoryEntity saved = categoryRepository.save(entity);

        return new ApiResponse<>("success", "Category updated successfully",
                CategoryMapper.toDto(saved));
    }

    @Override
    public ApiResponse<String> deleteOrDeactivateCategory(Long id) {

        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (entity.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Category already deactivated");
        }


        if (entity.getSubCategories() != null) {
            entity.getSubCategories().forEach(sub -> sub.setStatus(Status.INACTIVE));
        }

        entity.setStatus(Status.INACTIVE);
        entity.setUpdatedAt(LocalDateTime.now());

        categoryRepository.save(entity);

        return new ApiResponse<>("success", "Category deactivated successfully", null);
    }

    @Override
    public ApiResponse<Page<CategoryResponseDto>> getActiveCategories(
            int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryEntity> categoryPage =
                categoryRepository.findByStatus(Status.ACTIVE, pageable);

        if (categoryPage.isEmpty()) {
            return new ApiResponse<>("success", "No active categories found", Page.empty());
        }

        Page<CategoryResponseDto> dtoPage =
                categoryPage.map(CategoryMapper::toDto);

        return new ApiResponse<>(
                "success",
                "Active categories fetched successfully",
                dtoPage
        );
    }


    @Override
    public ApiResponse<List<CategoryWithProductsResponseDto>> getActiveCategoriesWithProducts() {

        List<CategoryEntity> categories =
                categoryRepository.findByStatus(Status.ACTIVE);

        if (categories.isEmpty()) {
            return new ApiResponse<>("success", "No active categories found", List.of());
        }

        List<CategoryWithProductsResponseDto> response = categories.stream()
                .map(category -> {

                    List<ProductResponseDto> products = category.getSubCategories().stream()
                            .filter(sub -> sub.getStatus() == Status.ACTIVE)
                            .flatMap(sub -> sub.getProduct().stream())
                            .map(ProductMapper::toDto)
                            .toList();

                    if (products.isEmpty()) {
                        return null;
                    }

                    CategoryWithProductsResponseDto dto = new CategoryWithProductsResponseDto();
                    dto.setId(category.getId());
                    dto.setCategoryName(category.getCategoryName());
                    dto.setProducts(products);

                    return dto;

                })
                .filter(java.util.Objects::nonNull)
                .toList();

        if (response.isEmpty()) {
            return new ApiResponse<>("success", "No active categories with products found", List.of());
        }

        return new ApiResponse<>("success", "Active categories with products fetched successfully", response);
    }
    @Override
    public Long getActiveCategoryCount() {
        return categoryRepository.countByStatus(Status.ACTIVE);
    }

}