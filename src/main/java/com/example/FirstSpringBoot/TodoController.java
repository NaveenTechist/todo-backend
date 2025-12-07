package com.example.FirstSpringBoot;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // CREATE
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        if (todo == null || todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty!");
        }
        if (todo.getStatus() == null || todo.getStatus().trim().isEmpty()) {
            todo.setStatus("pending");
        }
        return todoRepository.save(todo);
    }

    // READ ALL
    @GetMapping
    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    // UPDATE
    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        return todoRepository.findById(id).map(existingTodo -> {
            if (todo.getTitle() != null && !todo.getTitle().trim().isEmpty()) {
                existingTodo.setTitle(todo.getTitle());
            }
            if (todo.getDescription() != null) {
                existingTodo.setDescription(todo.getDescription());
            }
            if (todo.getStatus() != null && !todo.getStatus().trim().isEmpty()) {
                existingTodo.setStatus(todo.getStatus());
            }
            Todo savedTodo = java.util.Objects.requireNonNull(todoRepository.save(existingTodo));
            return savedTodo;
        }).orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null!");
        }
        return todoRepository.findById(id).map(existingTodo -> {
            if (existingTodo != null) {
                todoRepository.delete(existingTodo);
                return "Todo deleted successfully!";
            } else {
                throw new RuntimeException("Todo not found with id: " + id);
            }
        }).orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }
}
