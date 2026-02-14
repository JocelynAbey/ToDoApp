package com.jocelyn.todoapp.repository;

import com.jocelyn.todoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskListIdOrderByCreatedAtDesc(Long listId);

    List<Task> findByTaskListIdAndParentTaskIsNullOrderByCreatedAtDesc(Long listId);

    List<Task> findByParentTaskIdOrderByCreatedAtAsc(Long parentTaskId);

    List<Task> findByDueDate(LocalDate dueDate);

    List<Task> findByDueDateBetween(LocalDate start, LocalDate end);

    List<Task> findByDueDateBeforeAndCompletedFalse(LocalDate date);

    List<Task> findAllByOrderByCreatedAtDesc();

    boolean existsByParentTaskId(Long parentTaskId);

    long countByTaskListId(Long listId);

    @Modifying
    @Query(value = "UPDATE tasks SET parent_task_id = NULL WHERE list_id = :listId", nativeQuery = true)
    void clearParentReferencesByListId(@Param("listId") Long listId);

    void deleteByTaskListId(Long listId);
}
