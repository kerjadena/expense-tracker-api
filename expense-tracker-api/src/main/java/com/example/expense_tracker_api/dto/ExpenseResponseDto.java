package com.example.expense_tracker_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ExpenseResponseDto {
    private Long id;
    private Double amount;
    private LocalDate date;

    @JsonProperty ("category_name")
    private String categoryName;
    private String note;
}