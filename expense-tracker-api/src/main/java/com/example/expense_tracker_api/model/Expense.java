package com.example.expense_tracker_api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "expenses")
@Data
public class Expense {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @JsonProperty("user_id")
    private Long userId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "category_id", nullable = false)
    @JsonProperty("category_id")
    private Long categoryId;

    private String note;
}