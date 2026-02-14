package com.jocelyn.todoapp.repository;

import com.jocelyn.todoapp.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    Optional<TaskList> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
