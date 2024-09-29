package com.bigdata.ibmec.controller;

import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.service.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoControllerTest {

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private EnderecoController enderecoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarEnderecosPorClienteComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco1 = new Endereco();
        endereco1.setRua("Rua A");
        Endereco endereco2 = new Endereco();
        endereco2.setRua("Rua B");
        enderecos.add(endereco1);
        enderecos.add(endereco2);

        when(enderecoService.listarEnderecosPorCliente(1L)).thenReturn(enderecos);

        // Act
        ResponseEntity<List<Endereco>> response = enderecoController.listarEnderecosPorCliente(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(enderecoService, times(1)).listarEnderecosPorCliente(1L);
    }

    @Test
    void deveAdicionarEnderecoComSucesso() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Nova");

        when(enderecoService.adicionarEndereco(eq(1L), any(Endereco.class))).thenReturn(endereco);

        // Act
        ResponseEntity<Endereco> response = enderecoController.adicionarEndereco(1L, endereco);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rua Nova", response.getBody().getRua());
        verify(enderecoService, times(1)).adicionarEndereco(eq(1L), any(Endereco.class));
    }

    @Test
    void deveListarTodosOsEnderecosComSucesso() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco1 = new Endereco();
        endereco1.setRua("Rua A");
        Endereco endereco2 = new Endereco();
        endereco2.setRua("Rua B");
        enderecos.add(endereco1);
        enderecos.add(endereco2);

        when(enderecoService.listarTodos()).thenReturn(enderecos);

        // Act
        ResponseEntity<List<Endereco>> response = enderecoController.listarTodos();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(enderecoService, times(1)).listarTodos();
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        // Arrange
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Atualizada");

        when(enderecoService.atualizarEndereco(eq(1L), any(Endereco.class))).thenReturn(enderecoAtualizado);

        // Act
        ResponseEntity<Endereco> response = enderecoController.atualizarEndereco(1L, enderecoAtualizado);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rua Atualizada", response.getBody().getRua());
        verify(enderecoService, times(1)).atualizarEndereco(eq(1L), any(Endereco.class));
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        // Arrange
        doNothing().when(enderecoService).deletarEndereco(1L);

        // Act
        ResponseEntity<Void> response = enderecoController.deletarEndereco(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(enderecoService, times(1)).deletarEndereco(1L);
    }
}
