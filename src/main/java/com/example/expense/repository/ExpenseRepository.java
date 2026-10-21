
package com.example.expense.repository;

import com.example.expense.entity.Expense;
import com.example.expense.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findAllByOrderByDateDesc();

    List<Expense> findByCategoryOrderByDateDesc(
        ExpenseCategory category
    );
}