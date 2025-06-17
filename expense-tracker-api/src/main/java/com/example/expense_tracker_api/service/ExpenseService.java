package com.example.expense_tracker_api.service;

import com.example.expense_tracker_api.dto.ExpenseRequestDto;
import com.example.expense_tracker_api.dto.ExpenseResponseDto;
import com.example.expense_tracker_api.dto.ExpenseSummaryResponseDto;
import com.example.expense_tracker_api.model.Category;
import com.example.expense_tracker_api.model.Expense;
import com.example.expense_tracker_api.model.User;
import com.example.expense_tracker_api.repository.CategoryRepository;
import com.example.expense_tracker_api.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ExpenseResponseDto createExpense(ExpenseRequestDto dto, User user) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Expense expense = Expense.builder()
                .user(user)
                .amount(dto.getAmount())
                .date(dto.getDate())
                .category(category)
                .note(dto.getNote())
                .build();
        Expense saved = expenseRepository.save(expense);
        return toResponseDto(saved);
    }

    public List<ExpenseResponseDto> getExpenses(User user, LocalDate startDate, LocalDate endDate, Long categoryId) {
        List<Expense> expenses;
        if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);
        } else if (categoryId != null) {
            expenses = expenseRepository.findByUserIdAndCategoryId(user.getId(), categoryId);
        } else {
            expenses = expenseRepository.findByUserId(user.getId());
        }
        return expenses.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    public List<ExpenseSummaryResponseDto> getExpenseSummary(User user) {
        List<Expense> expenses = expenseRepository.findByUserId(user.getId());
        Map<String, Double> summary = new HashMap<>();
        for (Expense e : expenses) {
            String categoryName = e.getCategory().getName();
            summary.put(categoryName, summary.getOrDefault(categoryName, 0.0) + e.getAmount());
        }
        return summary.entrySet().stream()
                .map(entry -> {
                    ExpenseSummaryResponseDto dto = new ExpenseSummaryResponseDto();
                    dto.setCategoryName(entry.getKey());
                    dto.setTotalAmount(entry.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto dto, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized");
        }
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        expense.setAmount(dto.getAmount());
        expense.setDate(dto.getDate());
        expense.setCategory(category);
        expense.setNote(dto.getNote());
        Expense updated = expenseRepository.save(expense);
        return toResponseDto(updated);
    }

    @Transactional
    public void deleteExpense(Long id, User user) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found"));
        if (!expense.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Unauthorized");
        }
        expenseRepository.delete(expense);
    }

    private ExpenseResponseDto toResponseDto(Expense expense) {
        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setId(expense.getId());
        dto.setAmount(expense.getAmount());
        dto.setDate(expense.getDate());
        dto.setCategoryName(expense.getCategory().getName());
        dto.setNote(expense.getNote());
        return dto;
    }
}