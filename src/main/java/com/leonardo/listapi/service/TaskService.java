
package com.leonardo.listapi.service;

import com.leonardo.listapi.dto.TaskRequestDTO;
import com.leonardo.listapi.dto.TaskResponseDTO;
import com.leonardo.listapi.entity.Task;
import com.leonardo.listapi.entity.User;
import com.leonardo.listapi.repository.TaskRepository;
import com.leonardo.listapi.repository.UserRepository;
import com.leonardo.listapi.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Usuário autenticado não encontrado"
                        )
                );
    }

    private TaskResponseDTO toResponseDTO(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted()
        );
    }

    public TaskResponseDTO createTask(
            TaskRequestDTO request,
            String email
    ) {
        User user = getAuthenticatedUser(email);

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        return toResponseDTO(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> getAllTasks(String email) {
        User user = getAuthenticatedUser(email);

        return taskRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO getTaskById(Long id, String email) {
        User user = getAuthenticatedUser(email);

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException(id));

        return toResponseDTO(task);
    }

    public TaskResponseDTO updateTask(
            Long id,
            TaskRequestDTO request,
            String email
    ) {
        User user = getAuthenticatedUser(email);

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());

        Task savedTask = taskRepository.save(task);

        return toResponseDTO(savedTask);
    }

    public void deleteTask(Long id, String email) {
        User user = getAuthenticatedUser(email);

        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.delete(task);
    }
}
