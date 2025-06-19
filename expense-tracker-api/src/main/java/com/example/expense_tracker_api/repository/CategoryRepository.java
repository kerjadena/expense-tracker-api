package com.example.expense_tracker_api.repository;

import com.example.expense_tracker_api.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}