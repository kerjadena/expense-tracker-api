package com.example.expense_tracker_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ExpenseRequestDto {
    private Double amount;
    private LocalDate date;

    @JsonProperty("category_id")
    private Long categoryId;
    private String note;
}