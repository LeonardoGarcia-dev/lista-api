
package com.leonardo.listapi.service;

import com.leonardo.listapi.dto.TaskRequestDTO;
import com.leonardo.listapi.dto.TaskResponseDTO;
import com.leonardo.listapi.entity.Task;
import com.leonardo.listapi.entity.User;
import com.leonardo.listapi.repository.TaskRepository;
import com.leonardo.listapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import com.leonardo.listapi.exception.TaskNotFoundException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void deveCriarTarefaComSucesso() {
        // Arrange: preparar os dados do teste
        User user = new User();
        user.setId(1L);
        user.setEmail("leonardo.teste@example.com");

        TaskRequestDTO request = new TaskRequestDTO(
                "Estudar JUnit",
                "Criar meu primeiro teste automatizado",
                false
        );

        Task savedTask = new Task();
        savedTask.setId(10L);
        savedTask.setTitle(request.title());
        savedTask.setDescription(request.description());
        savedTask.setCompleted(request.completed());
        savedTask.setUser(user);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        // Act: executar o método que queremos testar
        TaskResponseDTO response =
                taskService.createTask(request, user.getEmail());

        // Assert: verificar o resultado
        assertEquals(10L, response.id());
        assertEquals("Estudar JUnit", response.title());
        assertEquals(
                "Criar meu primeiro teste automatizado",
                response.description()
        );
        assertFalse(response.completed());

        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void deveListarTarefasDoUsuario() {
        // Arrange: preparar o usuário
        User user = new User();
        user.setId(1L);
        user.setEmail("leonardo.teste@example.com");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Estudar Java");
        task.setDescription("Praticar testes automatizados");
        task.setCompleted(false);
        task.setUser(user);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.findAllByUserId(user.getId()))
                .thenReturn(List.of(task));

        // Act: executar o método de listagem
        List<TaskResponseDTO> response =
                taskService.getAllTasks(user.getEmail());

        // Assert: conferir o resultado
        assertEquals(1, response.size());
        assertEquals("Estudar Java", response.get(0).title());
        assertEquals(1L, response.get(0).id());
    }


    @Test
    void deveBuscarTarefaPorId() {
        // Arrange: preparar o usuário e a tarefa
        User user = new User();
        user.setId(1L);
        user.setEmail("leonardo.teste@example.com");

        Task task = new Task();
        task.setId(5L);
        task.setTitle("Estudar Spring");
        task.setDescription("Aprender a buscar tarefas por ID");
        task.setCompleted(false);
        task.setUser(user);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.findByIdAndUserId(5L, user.getId()))
                .thenReturn(Optional.of(task));

        // Act: buscar a tarefa
        TaskResponseDTO response =
                taskService.getTaskById(5L, user.getEmail());

        // Assert: verificar o resultado
        assertEquals(5L, response.id());
        assertEquals("Estudar Spring", response.title());
        assertEquals(
                "Aprender a buscar tarefas por ID",
                response.description()
        );
        assertFalse(response.completed());
    }


    @Test
    void deveLancarExcecaoQuandoTarefaNaoExistir() {
        // Arrange: preparar o usuário
        User user = new User();
        user.setId(1L);
        user.setEmail("leonardo.teste@example.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.findByIdAndUserId(999L, user.getId()))
                .thenReturn(Optional.empty());

        // Act + Assert: verificar se a exceção é lançada
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(999L, user.getEmail())
        );
    }


    @Test
    void deveAtualizarTarefaComSucesso() {
        // Arrange: preparar usuário, tarefa e novos dados
        User user = new User();
        user.setId(1L);
        user.setEmail("leonardo.teste@example.com");

        Task task = new Task();
        task.setId(5L);
        task.setTitle("Título antigo");
        task.setDescription("Descrição antiga");
        task.setCompleted(false);
        task.setUser(user);

        TaskRequestDTO request = new TaskRequestDTO(
                "Título atualizado",
                "Descrição atualizada",
                true
        );

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.findByIdAndUserId(5L, user.getId()))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: executar a atualização
        TaskResponseDTO response =
                taskService.updateTask(5L, request, user.getEmail());

        // Assert: verificar os novos dados
        assertEquals(5L, response.id());
        assertEquals("Título atualizado", response.title());
        assertEquals("Descrição atualizada", response.description());
        assertTrue(response.completed());

        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void deveExcluirTarefaComSucesso() {
        // Arrange: preparar o usuário e a tarefa
        User user = new User();
        user.setId(1L);
        user.setEmail("leonardo.teste@example.com");

        Task task = new Task();
        task.setId(5L);
        task.setTitle("Tarefa para excluir");
        task.setDescription("Testar exclusão");
        task.setCompleted(false);
        task.setUser(user);

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.findByIdAndUserId(5L, user.getId()))
                .thenReturn(Optional.of(task));

        // Act: executar a exclusão
        taskService.deleteTask(5L, user.getEmail());

        // Assert: verificar se a tarefa foi excluída
        verify(taskRepository).delete(task);
    }


    @Test
    void deveImpedirExclusaoDeTarefaDeOutroUsuario() {
        User user = new User();
        user.setId(2L);
        user.setEmail("usuario2.teste@example.com");

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));

        when(taskRepository.findByIdAndUserId(5L, user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(5L, user.getEmail())
        );

        verify(taskRepository, never()).delete(any(Task.class));
    }

}
