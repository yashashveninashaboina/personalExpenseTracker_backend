package com.example.expense.controller;

import com.example.expense.entity.Expense;
import com.example.expense.entity.ExpenseCategory;
import com.example.expense.service.ExpenseService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController {

    private final ExpenseService service;

    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Expense> getAll(
            @RequestParam(required = false)
            ExpenseCategory category) {
        return service.getAll(category);
    }

    @GetMapping("/{id}")
    public Expense getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<Expense> create(
            @Valid @RequestBody Expense expense) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(expense));
    }

    @PutMapping("/{id}")
    public Expense update(
            @PathVariable Long id,
            @Valid @RequestBody Expense expense) {
        return service.update(id, expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(
        ExpenseService.ExpenseNotFoundException.class
    )
    public ResponseEntity<Map<String, String>> notFound(
            RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(
        org.springframework.web.bind.MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, String>> validationError(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("Invalid expense details");

        return ResponseEntity.badRequest()
            .body(Map.of("message", message));
    }

    @ExceptionHandler(
        org.springframework.http.converter.HttpMessageNotReadableException.class
    )
    public ResponseEntity<Map<String, String>> invalidRequest(
            Exception ex) {
        return ResponseEntity.badRequest()
            .body(Map.of(
                "message",
                "Invalid request. Check the date and category values."
            ));
    }

    @ExceptionHandler(
        org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class
    )
    public ResponseEntity<Map<String, String>> invalidParameter(
            Exception ex) {
        return ResponseEntity.badRequest()
            .body(Map.of(
                "message",
                "Invalid expense ID or category."
            ));
    }
}