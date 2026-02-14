package com.jocelyn.todoapp.dto;

import com.jocelyn.todoapp.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 255, message = "Task title cannot exceed 255 characters")
    private String title;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    private boolean completed;

    private LocalDate dueDate;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @Size(max = 5000, message = "Notes cannot exceed 5000 characters")
    private String notes;

    @Size(max = 255, message = "Location cannot exceed 255 characters")
    private String location;

    @Size(max = 1024, message = "Meeting link cannot exceed 1024 characters")
    private String meetingLink;

    @NotNull(message = "List id is required")
    private Long listId;

    private Long parentTaskId;
}
