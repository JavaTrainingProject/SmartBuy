package com.example.smartbuy.serviceImpls;

import com.example.smartbuy.dtos.SubCategoryRequestDto;
import com.example.smartbuy.dtos.SubCategoryResponseDto;
import com.example.smartbuy.entity.CategoryEntity;
import com.example.smartbuy.entity.SubCategoryEntity;
import com.example.smartbuy.enums.Status;
import com.example.smartbuy.mapper.SubCategoryMapper;
import com.example.smartbuy.repository.CategoryRepository;
import com.example.smartbuy.repository.SubCategoryRepository;
import com.example.smartbuy.service.SubCategoryService;
import org.springframework.stereotype.Service;

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
}