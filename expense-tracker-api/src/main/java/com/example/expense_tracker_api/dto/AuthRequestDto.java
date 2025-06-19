package com.example.expense_tracker_api.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;
}