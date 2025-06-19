package com.example.expense_tracker_api.controller;

import com.example.expense_tracker_api.dto.ExpenseRequestDto;
import com.example.expense_tracker_api.dto.ExpenseResponseDto;
import com.example.expense_tracker_api.dto.ExpenseSummaryResponseDto;
import com.example.expense_tracker_api.model.User;
import com.example.expense_tracker_api.repository.UserRepository;
import com.example.expense_tracker_api.service.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public ExpenseResponseDto createExpense(
            @RequestBody ExpenseRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return expenseService.createExpense(dto, user);
    }

    @GetMapping
    public List<ExpenseResponseDto> getExpenses(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long categoryId) {
        User user = getCurrentUser(userDetails);
        return expenseService.getExpenses(user, startDate, endDate, categoryId);
    }

    @GetMapping("/summary")
    public List<ExpenseSummaryResponseDto> getExpenseSummary(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return expenseService.getExpenseSummary(user);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        return expenseService.updateExpense(id, dto, user);
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = getCurrentUser(userDetails);
        expenseService.deleteExpense(id, user);
    }
}