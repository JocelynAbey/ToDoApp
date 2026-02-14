package com.jocelyn.todoapp.dto;

import com.jocelyn.todoapp.model.Priority;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDate dueDate;
    private Priority priority;
    private String notes;
    private String location;
    private String meetingLink;
    private Long listId;
    private Long parentTaskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TaskResponse> children;
}
