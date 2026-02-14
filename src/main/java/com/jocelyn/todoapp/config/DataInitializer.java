package com.jocelyn.todoapp.config;

import com.jocelyn.todoapp.model.TaskList;
import com.jocelyn.todoapp.repository.TaskListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TaskListRepository taskListRepository;

    @Override
    public void run(String... args) {
        List<String> systemLists = List.of("Shopping", "Personal", "Work");
        for (String name : systemLists) {
            taskListRepository.findByNameIgnoreCase(name)
                    .orElseGet(() -> taskListRepository.save(
                            TaskList.builder()
                                    .name(name)
                                    .systemDefined(true)
                                    .build()
                    ));
        }
    }
}
