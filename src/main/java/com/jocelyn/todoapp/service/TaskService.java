package com.jocelyn.todoapp.service;

import com.jocelyn.todoapp.dto.TaskMoveRequest;
import com.jocelyn.todoapp.dto.TaskRequest;
import com.jocelyn.todoapp.dto.TaskResponse;
import com.jocelyn.todoapp.exception.BadRequestException;
import com.jocelyn.todoapp.exception.ResourceNotFoundException;
import com.jocelyn.todoapp.model.Task;
import com.jocelyn.todoapp.model.TaskList;
import com.jocelyn.todoapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListService taskListService;

    public TaskResponse create(TaskRequest request) {
        TaskList taskList = taskListService.findById(request.getListId());
        Task parent = resolveParentForList(request.getParentTaskId(), taskList.getId());

        Task task = Task.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .completed(request.isCompleted())
                .dueDate(request.getDueDate())
                .priority(request.getPriority())
                .notes(request.getNotes())
                .location(request.getLocation())
                .meetingLink(request.getMeetingLink())
                .taskList(taskList)
                .parentTask(parent)
                .build();

        return toResponse(taskRepository.save(task), false, Map.of());
    }

    public TaskResponse update(Long taskId, TaskRequest request) {
        Task existing = findById(taskId);
        TaskList taskList = taskListService.findById(request.getListId());
        Task parent = resolveParentForList(request.getParentTaskId(), taskList.getId());

        if (parent != null) {
            validateNoCircularReference(existing.getId(), parent);
        }

        existing.setTitle(request.getTitle().trim());
        existing.setDescription(request.getDescription());
        existing.setCompleted(request.isCompleted());
        existing.setDueDate(request.getDueDate());
        existing.setPriority(request.getPriority());
        existing.setNotes(request.getNotes());
        existing.setLocation(request.getLocation());
        existing.setMeetingLink(request.getMeetingLink());
        existing.setTaskList(taskList);
        existing.setParentTask(parent);

        return toResponse(taskRepository.save(existing), false, Map.of());
    }

    @Transactional
    public void delete(Long taskId) {
        Task task = findById(taskId);
        deleteTaskRecursively(task);
    }

    public TaskResponse move(Long taskId, TaskMoveRequest request) {
        Task task = findById(taskId);
        Long targetListId = request.getListId() != null ? request.getListId() : task.getTaskList().getId();
        TaskList targetList = taskListService.findById(targetListId);

        Task parent = resolveParentForList(request.getParentTaskId(), targetListId);
        if (parent != null) {
            validateNoCircularReference(taskId, parent);
        }

        task.setTaskList(targetList);
        task.setParentTask(parent);
        return toResponse(taskRepository.save(task), false, Map.of());
    }

    public List<TaskResponse> getByList(Long listId) {
        taskListService.findById(listId);
        return taskRepository.findByTaskListIdOrderByCreatedAtDesc(listId)
                .stream()
                .map(task -> toResponse(task, false, Map.of()))
                .toList();
    }

    public List<TaskResponse> getTreeByList(Long listId) {
        taskListService.findById(listId);
        List<Task> tasks = taskRepository.findByTaskListIdOrderByCreatedAtDesc(listId);
        return buildTree(tasks);
    }

    public List<TaskResponse> getSmartView(String view) {
        LocalDate today = LocalDate.now();
        return switch (view.toLowerCase()) {
            case "today" -> taskRepository.findByDueDate(today).stream().map(t -> toResponse(t, false, Map.of())).toList();
            case "week", "this-week", "thisweek" -> {
                LocalDate start = today.with(DayOfWeek.MONDAY);
                LocalDate end = today.with(DayOfWeek.SUNDAY);
                yield taskRepository.findByDueDateBetween(start, end).stream().map(t -> toResponse(t, false, Map.of())).toList();
            }
            case "overdue" -> taskRepository.findByDueDateBeforeAndCompletedFalse(today)
                    .stream()
                    .map(t -> toResponse(t, false, Map.of()))
                    .toList();
            case "all", "all-tasks" -> taskRepository.findAllByOrderByCreatedAtDesc().stream().map(t -> toResponse(t, false, Map.of())).toList();
            default -> throw new BadRequestException("Unsupported smart view: " + view);
        };
    }

    private Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    private Task resolveParentForList(Long parentTaskId, Long listId) {
        if (parentTaskId == null) {
            return null;
        }
        Task parent = findById(parentTaskId);
        if (!parent.getTaskList().getId().equals(listId)) {
            throw new BadRequestException("Parent task must belong to the same list");
        }
        return parent;
    }

    private void validateNoCircularReference(Long taskId, Task candidateParent) {
        if (taskId.equals(candidateParent.getId())) {
            throw new BadRequestException("A task cannot be parent of itself");
        }

        Task current = candidateParent;
        while (current != null) {
            if (taskId.equals(current.getId())) {
                throw new BadRequestException("Circular hierarchy detected");
            }
            current = current.getParentTask();
        }
    }

    private List<TaskResponse> buildTree(List<Task> tasks) {
        Map<Long, List<Task>> childrenByParent = new HashMap<>();
        List<Task> roots = new ArrayList<>();

        for (Task task : tasks) {
            Task parent = task.getParentTask();
            if (parent == null) {
                roots.add(task);
            } else {
                childrenByParent.computeIfAbsent(parent.getId(), key -> new ArrayList<>()).add(task);
            }
        }

        return roots.stream()
                .map(root -> toResponse(root, true, childrenByParent))
                .toList();
    }

    private TaskResponse toResponse(Task task, boolean includeChildren, Map<Long, List<Task>> childrenMap) {
        List<TaskResponse> children = List.of();
        if (includeChildren) {
            children = childrenMap.getOrDefault(task.getId(), List.of())
                    .stream()
                    .map(child -> toResponse(child, true, childrenMap))
                    .toList();
        }

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .dueDate(task.getDueDate())
                .priority(task.getPriority())
                .notes(task.getNotes())
                .location(task.getLocation())
                .meetingLink(task.getMeetingLink())
                .listId(task.getTaskList().getId())
                .parentTaskId(task.getParentTask() != null ? task.getParentTask().getId() : null)
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .children(children)
                .build();
    }

    private void deleteTaskRecursively(Task task) {
        List<Task> children = taskRepository.findByParentTaskIdOrderByCreatedAtAsc(task.getId());
        for (Task child : children) {
            deleteTaskRecursively(child);
        }
        taskRepository.delete(task);
    }
}
