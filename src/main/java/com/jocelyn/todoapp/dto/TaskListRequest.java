package com.jocelyn.todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskListRequest {

    @NotBlank(message = "List name is required")
    @Size(max = 100, message = "List name cannot exceed 100 characters")
    private String name;
}
