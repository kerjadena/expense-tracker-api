package com.example.expense_tracker_api.controller;

import com.example.expense_tracker_api.dto.CategoryResponseDto;
import com.example.expense_tracker_api.model.Category;
import com.example.expense_tracker_api.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryService.findAll();
        return categories.stream().map(this::toDto).collect(Collectors.toList());
    }

    private CategoryResponseDto toDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}