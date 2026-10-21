package com.example.expense.service;

import com.example.expense.entity.Expense;
import com.example.expense.entity.ExpenseCategory;
import com.example.expense.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public List<Expense> getAll(ExpenseCategory category) {
        if (category == null) {
            return repository.findAllByOrderByDateDesc();
        }

        return repository.findByCategoryOrderByDateDesc(category);
    }

    public Expense getById(Long id) {
        return repository.findById(id)
            .orElseThrow(() ->
                new ExpenseNotFoundException(id));
    }

    public Expense create(Expense expense) {
        expense.setId(null);
        return repository.save(expense);
    }

    @Transactional
    public Expense update(Long id, Expense input) {
        Expense existing = getById(id);

        existing.setTitle(input.getTitle().trim());
        existing.setAmount(input.getAmount());
        existing.setCategory(input.getCategory());
        existing.setDate(input.getDate());

        return repository.save(existing);
    }

    public void delete(Long id) {
        Expense expense = getById(id);
        repository.delete(expense);
    }

    public static class ExpenseNotFoundException
            extends RuntimeException {

        public ExpenseNotFoundException(Long id) {
            super("Expense not found with ID: " + id);
        }
    }
}