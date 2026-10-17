
package com.leonardo.listapi.controller;

import com.leonardo.listapi.dto.TaskRequestDTO;
import com.leonardo.listapi.dto.TaskResponseDTO;
import com.leonardo.listapi.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @Valid @RequestBody TaskRequestDTO request,
            Principal principal
    ) {
        TaskResponseDTO createdTask =
                taskService.createTask(request, principal.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks(
            Principal principal
    ) {
        return ResponseEntity.ok(
                taskService.getAllTasks(principal.getName())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(
                taskService.getTaskById(id, principal.getName())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDTO request,
            Principal principal
    ) {
        TaskResponseDTO updatedTask =
                taskService.updateTask(id, request, principal.getName());

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Principal principal
    ) {
        taskService.deleteTask(id, principal.getName());

        return ResponseEntity.noContent().build();
    }
}
