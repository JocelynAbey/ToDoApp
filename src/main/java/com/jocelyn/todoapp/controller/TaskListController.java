package com.jocelyn.todoapp.controller;

import com.jocelyn.todoapp.dto.TaskListRequest;
import com.jocelyn.todoapp.dto.TaskListResponse;
import com.jocelyn.todoapp.service.TaskListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService taskListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListResponse create(@Valid @RequestBody TaskListRequest request) {
        return taskListService.create(request);
    }

    @GetMapping
    public List<TaskListResponse> getAll() {
        return taskListService.getAll();
    }

    @PutMapping("/{id}")
    public TaskListResponse update(@PathVariable Long id, @Valid @RequestBody TaskListRequest request) {
        return taskListService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskListService.delete(id);
    }
}
