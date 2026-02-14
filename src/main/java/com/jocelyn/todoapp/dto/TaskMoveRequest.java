package com.jocelyn.todoapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskMoveRequest {
    private Long parentTaskId;
    private Long listId;
}
