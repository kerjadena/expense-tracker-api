package com.example.expense_tracker_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExpenseSummaryResponseDto {
    @JsonProperty("category_name")
    private String categoryName;

    @JsonProperty("total_amount")
    private Double totalAmount;
}