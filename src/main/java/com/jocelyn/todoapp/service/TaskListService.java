package com.jocelyn.todoapp.service;

import com.jocelyn.todoapp.dto.TaskListRequest;
import com.jocelyn.todoapp.dto.TaskListResponse;
import com.jocelyn.todoapp.exception.BadRequestException;
import com.jocelyn.todoapp.exception.DuplicateResourceException;
import com.jocelyn.todoapp.exception.ResourceNotFoundException;
import com.jocelyn.todoapp.model.TaskList;
import com.jocelyn.todoapp.repository.TaskListRepository;
import com.jocelyn.todoapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final TaskRepository taskRepository;

    public TaskListResponse create(TaskListRequest request) {
        String normalizedName = request.getName().trim();
        if (taskListRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateResourceException("A list with this name already exists");
        }

        TaskList taskList = TaskList.builder()
                .name(normalizedName)
                .systemDefined(false)
                .build();

        return toResponse(taskListRepository.save(taskList));
    }

    public List<TaskListResponse> getAll() {
        return taskListRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskListResponse update(Long id, TaskListRequest request) {
        TaskList taskList = findById(id);
        String normalizedName = request.getName().trim();
        boolean duplicate = taskListRepository.findByNameIgnoreCase(normalizedName)
                .filter(existing -> !existing.getId().equals(id))
                .isPresent();

        if (duplicate) {
            throw new DuplicateResourceException("A list with this name already exists");
        }

        taskList.setName(normalizedName);
        return toResponse(taskListRepository.save(taskList));
    }

    @Transactional
    public void delete(Long id) {
        TaskList taskList = findById(id);
        if (taskList.isSystemDefined()) {
            throw new BadRequestException("System-defined lists cannot be deleted");
        }
        taskRepository.clearParentReferencesByListId(id);
        taskRepository.deleteByTaskListId(id);
        taskListRepository.delete(taskList);
    }

    public TaskList findById(Long id) {
        return taskListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("List not found with id: " + id));
    }

    private TaskListResponse toResponse(TaskList taskList) {
        return TaskListResponse.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .systemDefined(taskList.isSystemDefined())
                .createdAt(taskList.getCreatedAt())
                .updatedAt(taskList.getUpdatedAt())
                .build();
    }
}
