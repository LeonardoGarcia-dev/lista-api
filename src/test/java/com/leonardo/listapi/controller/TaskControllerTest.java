
package com.leonardo.listapi.controller;

import com.leonardo.listapi.dto.TaskRequestDTO;
import com.leonardo.listapi.dto.TaskResponseDTO;
import com.leonardo.listapi.exception.GlobalExceptionHandler;
import com.leonardo.listapi.exception.TaskNotFoundException;
import com.leonardo.listapi.service.JwtService;
import com.leonardo.listapi.service.TaskService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtService jwtService;

    private static final String EMAIL = "leonardo.teste@example.com";

    @Test
    void deveCriarTarefaComSucesso() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO(
                "Estudar JUnit",
                "Testar o controller",
                false
        );

        TaskResponseDTO response = new TaskResponseDTO(
                10L,
                "Estudar JUnit",
                "Testar o controller",
                false
        );

        when(taskService.createTask(any(TaskRequestDTO.class), eq(EMAIL)))
                .thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .principal(() -> EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Estudar JUnit"))
                .andExpect(jsonPath("$.description").value("Testar o controller"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).createTask(any(TaskRequestDTO.class), eq(EMAIL));
    }

    @Test
    void deveListarTarefasComSucesso() throws Exception {
        List<TaskResponseDTO> tasks = List.of(
                new TaskResponseDTO(
                        1L,
                        "Estudar Java",
                        "Praticar testes",
                        false
                ),
                new TaskResponseDTO(
                        2L,
                        "Estudar Spring",
                        "Revisar controllers",
                        true
                )
        );

        when(taskService.getAllTasks(EMAIL)).thenReturn(tasks);

        mockMvc.perform(get("/tasks")
                .principal(() -> EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Estudar Java"))
                .andExpect(jsonPath("$[1].title").value("Estudar Spring"));

        verify(taskService).getAllTasks(EMAIL);
    }

    @Test
    void deveBuscarTarefaPorIdComSucesso() throws Exception {
        TaskResponseDTO response = new TaskResponseDTO(
                5L,
                "Estudar Spring",
                "Aprender a buscar por ID",
                false
        );

        when(taskService.getTaskById(5L, EMAIL))
                .thenReturn(response);

        mockMvc.perform(get("/tasks/{id}", 5L)
                        .principal(() -> EMAIL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Estudar Spring"))
                .andExpect(jsonPath("$.description").value("Aprender a buscar por ID"))
                .andExpect(jsonPath("$.completed").value(false));

        verify(taskService).getTaskById(5L, EMAIL);
    }

    @Test
    void deveRetornar404QuandoTarefaNaoExistir() throws Exception {
        when(taskService.getTaskById(999L, EMAIL))
                .thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(get("/tasks/{id}", 999L)
                        .principal(() -> EMAIL))
                .andExpect(status().isNotFound());

        verify(taskService).getTaskById(999L, EMAIL);
    }

    @Test
    void deveAtualizarTarefaComSucesso() throws Exception {
        TaskRequestDTO request = new TaskRequestDTO(
                "Título atualizado",
                "Descrição atualizada",
                true
        );

        TaskResponseDTO response = new TaskResponseDTO(
                5L,
                "Título atualizado",
                "Descrição atualizada",
                true
        );

        when(taskService.updateTask(eq(5L), any(TaskRequestDTO.class), eq(EMAIL)))
                .thenReturn(response);

        mockMvc.perform(put("/tasks/{id}", 5L)
                        .principal(() -> EMAIL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("Título atualizado"))
                .andExpect(jsonPath("$.description").value("Descrição atualizada"))
                .andExpect(jsonPath("$.completed").value(true));

        verify(taskService).updateTask(eq(5L), any(TaskRequestDTO.class), eq(EMAIL));
    }

    @Test
    void deveExcluirTarefaComSucesso() throws Exception {
        doNothing().when(taskService).deleteTask(5L, EMAIL);

        mockMvc.perform(delete("/tasks/{id}", 5L)
                .principal(() -> EMAIL))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(5L, EMAIL);
    }
}
