package com.example.expense_tracker_api.controller;

import com.example.expense_tracker_api.dto.ExpenseRequestDto;
import com.example.expense_tracker_api.dto.ExpenseResponseDto;
import com.example.expense_tracker_api.dto.ExpenseSummaryResponseDto;
import com.example.expense_tracker_api.model.User;
import com.example.expense_tracker_api.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseResponseDto createExpense(
            @RequestBody ExpenseRequestDto dto,
            @AuthenticationPrincipal User user) {
        return expenseService.createExpense(dto, user);
    }

    @GetMapping
    public List<ExpenseResponseDto> getExpenses(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long categoryId) {
        return expenseService.getExpenses(user, startDate, endDate, categoryId);
    }

    @GetMapping("/summary")
    public List<ExpenseSummaryResponseDto> getExpenseSummary(@AuthenticationPrincipal User user) {
        return expenseService.getExpenseSummary(user);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequestDto dto,
            @AuthenticationPrincipal User user) {
        return expenseService.updateExpense(id, dto, user);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        expenseService.deleteExpense(id, user);
    }
}