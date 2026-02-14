package com.jocelyn.todoapp.controller;

import com.jocelyn.todoapp.dto.TaskMoveRequest;
import com.jocelyn.todoapp.dto.TaskRequest;
import com.jocelyn.todoapp.dto.TaskResponse;
import com.jocelyn.todoapp.service.TaskService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @PutMapping("/{id}/move")
    public TaskResponse move(@PathVariable Long id, @RequestBody TaskMoveRequest request) {
        return taskService.move(id, request);
    }

    @GetMapping("/list/{listId}")
    public List<TaskResponse> getByList(@PathVariable Long listId) {
        return taskService.getByList(listId);
    }

    @GetMapping("/list/{listId}/tree")
    public List<TaskResponse> getTreeByList(@PathVariable Long listId) {
        return taskService.getTreeByList(listId);
    }

    @GetMapping("/smart-view")
    public List<TaskResponse> getSmartView(@RequestParam String view) {
        return taskService.getSmartView(view);
    }
}
