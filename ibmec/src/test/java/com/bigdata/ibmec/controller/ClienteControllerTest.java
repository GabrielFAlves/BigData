package com.bigdata.ibmec.controller;

import com.bigdata.ibmec.DTO.ClienteComEnderecoDTO;
import com.bigdata.ibmec.exceptions.ClienteNotFoundException;
import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.service.ClienteService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAdicionarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1)); // Maior de 18 anos

        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01000-000");

        ClienteComEnderecoDTO clienteComEnderecoDTO = new ClienteComEnderecoDTO();
        clienteComEnderecoDTO.setCliente(cliente);
        clienteComEnderecoDTO.setEndereco(endereco);

        when(clienteService.adicionarClienteComEndereco(clienteComEnderecoDTO)).thenReturn(cliente);

        // Act
        ResponseEntity<Cliente> response = clienteController.adicionarCliente(clienteComEnderecoDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("João Silva", response.getBody().getNome());
        verify(clienteService, times(1)).adicionarClienteComEndereco(clienteComEnderecoDTO);
    }

    @Test
    void deveFalharAoAdicionarClienteComDadosInvalidos() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("");  // Nome inválido (vazio)
        cliente.setEmail("emailinvalido");  // Email inválido
        cliente.setCpf("12345678900");  // CPF inválido
        cliente.setDataNascimento(LocalDate.of(2025, 1, 1));  // Data no futuro

        Endereco endereco = new Endereco();
        endereco.setRua("");  // Rua inválida (vazia)
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("00000-000");  // CEP inválido

        ClienteComEnderecoDTO clienteComEnderecoDTO = new ClienteComEnderecoDTO();
        clienteComEnderecoDTO.setCliente(cliente);
        clienteComEnderecoDTO.setEndereco(endereco);

        // Simular a violação de validação
        doThrow(new ConstraintViolationException("Dados inválidos", Set.of())).when(clienteService).adicionarClienteComEndereco(any(ClienteComEnderecoDTO.class));

        // Act
        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            clienteController.adicionarCliente(clienteComEnderecoDTO);
        });

        // Assert
        assertNotNull(exception);
        assertEquals("Dados inválidos", exception.getMessage());
    }

    @Test
    void deveFalharAoBuscarClienteInexistente() {
        // Arrange
        when(clienteService.buscarClientePorId(1L)).thenThrow(new ClienteNotFoundException(1L));

        // Act
        Exception exception = assertThrows(ClienteNotFoundException.class, () -> {
            clienteController.buscarClientePorId(1L);
        });

        // Assert
        assertNotNull(exception);
        assertEquals("Cliente com ID 1 não encontrado", exception.getMessage());
    }

    @Test
    void deveFalharAoAtualizarClienteInexistente() {
        // Arrange
        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Atualizado");

        when(clienteService.atualizarDadosCliente(anyLong(), any(Cliente.class))).thenThrow(new ClienteNotFoundException(1L));

        // Act
        Exception exception = assertThrows(ClienteNotFoundException.class, () -> {
            clienteController.atualizarCliente(1L, clienteAtualizado);
        });

        // Assert
        assertNotNull(exception);
        assertEquals("Cliente com ID 1 não encontrado", exception.getMessage());
    }

    @Test
    void deveDeletarClienteComSucesso() {
        // Arrange
        doNothing().when(clienteService).deletarCliente(1L);

        // Act
        ResponseEntity<Void> response = clienteController.deletarCliente(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clienteService, times(1)).deletarCliente(1L);
    }

    @Test
    void deveFalharAoDeletarClienteInexistente() {
        // Arrange
        doThrow(new ClienteNotFoundException(1L)).when(clienteService).deletarCliente(1L);

        // Act
        Exception exception = assertThrows(ClienteNotFoundException.class, () -> {
            clienteController.deletarCliente(1L);
        });

        // Assert
        assertNotNull(exception);
        assertEquals("Cliente com ID 1 não encontrado", exception.getMessage());
    }
}
