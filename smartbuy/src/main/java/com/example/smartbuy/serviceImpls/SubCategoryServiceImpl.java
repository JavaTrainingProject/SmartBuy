package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.entity.SubCategoryEntity;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.exception.ResourceNotFoundException;
import com.example.smartbuy.mapper.SubCategoryMapper;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.response.ApiResponse;
import com.example.smartbuy.service.SubCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository,
                                  CategoryRepository categoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public SubCategoryResponseDto createSubCategory(SubCategoryRequestDto dto) {

        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (subCategoryRepository.existsBySubCategoryNameIgnoreCase(dto.getSubCategoryName())) {
            throw new RuntimeException("SubCategory already exists");
        }

        SubCategoryEntity entity = SubCategoryMapper.toEntity(dto);

        entity.setCategory(category);

        if (entity.getStatus() == null) {
            entity.setStatus(Status.ACTIVE);
        }

        SubCategoryEntity saved = subCategoryRepository.save(entity);

        return SubCategoryMapper.toDto(saved);
    }
    @Override
    public SubCategoryResponseDto getSubCategoryById(Long id) {
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
        return SubCategoryMapper.toDto(subCategoryEntity);
    }

    @Override
    public ApiResponse<List<SubCategoryResponseDto>> getAllActiveSubCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<SubCategoryEntity> subCategoryPage = subCategoryRepository.findByStatus(Status.ACTIVE,pageable);

        List<SubCategoryResponseDto> responseList = subCategoryPage.getContent()
                .stream()
                .map(SubCategoryMapper::toDto)
                .toList();

        return new ApiResponse<>("SUCCESS",
                "Active subcategories fetched successfully",responseList
        );
    }

    @Override
    public ApiResponse<List<SubCategoryResponseDto>> getActiveSubCategoriesByCategory(Long categoryId, int page, int size) {

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<SubCategoryEntity> subCategoryPage =
                subCategoryRepository.findByCategoryIdAndStatus(
                        categoryId,
                        Status.ACTIVE,
                        pageable
                );

        List<SubCategoryResponseDto> responseList = subCategoryPage.getContent()
                .stream()
                .map(SubCategoryMapper::toDto)
                .toList();

        return new ApiResponse<>("SUCCESS",
                "Active subcategories fetched successfully",
                 responseList);
    }

    @Override
    public ApiResponse<String> updateSubCategoryStatus(Long id, Status status) {
        SubCategoryEntity subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));
        subCategory.setStatus(status);

        subCategoryRepository.save(subCategory);

        return new ApiResponse<>(
                "SubCategory status updated successfully",
                String.valueOf(true),
                "DONE"
        );
    }

    @Override
    public SubCategoryResponseDto updateSubCategory(Long id, SubCategoryRequestDto dto) {
        SubCategoryEntity subCategoryEntity = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory", "id", id));

        CategoryEntity categoryEntity = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));

        subCategoryEntity.setSubCategoryName(dto.getSubCategoryName());
        subCategoryEntity.setSubCategoryDescription(dto.getSubCategoryDescription());
        subCategoryEntity.setStatus(dto.getStatus());
        subCategoryEntity.setCategory(categoryEntity);

        SubCategoryEntity updated = subCategoryRepository.save(subCategoryEntity);
        return SubCategoryMapper.toDto(updated);
    }

    @Override
    public ApiResponse<List<SubCategoryResponseDto>> getAllSubCategories() {
        List<SubCategoryEntity> list = subCategoryRepository.findAll();

        List<SubCategoryResponseDto> response =
                list.stream()
                        .map(SubCategoryMapper::toDto)
                        .toList();

        return new ApiResponse<>("SUCCESS", "Subcategories fetched successfully", response);
    }

    @Override
    public ApiResponse<List<SubCategoryResponseDto>> getByStatus(Status status) {
        List<SubCategoryEntity> list = subCategoryRepository.findByStatus(status);

        List<SubCategoryResponseDto> response =
                list.stream()
                        .map(SubCategoryMapper::toDto)
                        .toList();

        return new ApiResponse<>("SUCCESS", "Subcategories fetched successfully", response);
    }
    @Override
    public void softDeleteSubCategory(Long id) {

        SubCategoryEntity subCategory =
                subCategoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "SubCategory not found"));

        subCategory.setStatus(Status.INACTIVE);

        subCategoryRepository.save(subCategory);
    }
}