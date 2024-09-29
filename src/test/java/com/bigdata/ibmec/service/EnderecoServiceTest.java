package com.bigdata.ibmec.service;

import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.repository.ClienteRepository;
import com.bigdata.ibmec.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarEnderecosPorClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");
        enderecos.add(endereco);
        cliente.setEnderecos(enderecos);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        List<Endereco> resultado = enderecoService.listarEnderecosPorCliente(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Rua A", resultado.get(0).getRua());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveFalharAoListarEnderecosDeClienteInexistente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> enderecoService.listarEnderecosPorCliente(1L));
        assertEquals("Cliente não encontrado", thrown.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveAdicionarEnderecoComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEnderecos(new ArrayList<>());

        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        // Act
        Endereco resultado = enderecoService.adicionarEndereco(1L, endereco);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rua A", resultado.getRua());
        assertEquals(cliente, resultado.getCliente());
        verify(clienteRepository, times(1)).findById(1L);
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    void deveFalharAoAdicionarEnderecoAClienteInexistente() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> enderecoService.adicionarEndereco(1L, endereco));
        assertEquals("Cliente não encontrado", thrown.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveListarTodosOsEnderecos() {
        // Arrange
        List<Endereco> enderecos = new ArrayList<>();
        Endereco endereco1 = new Endereco();
        endereco1.setRua("Rua A");
        Endereco endereco2 = new Endereco();
        endereco2.setRua("Rua B");
        enderecos.add(endereco1);
        enderecos.add(endereco2);

        when(enderecoRepository.findAll()).thenReturn(enderecos);

        // Act
        List<Endereco> resultado = enderecoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(enderecoRepository, times(1)).findAll();
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        // Arrange
        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(1L);
        enderecoExistente.setRua("Rua A");

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua B");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(enderecoExistente));
        when(enderecoRepository.save(enderecoExistente)).thenReturn(enderecoExistente);

        // Act
        Endereco resultado = enderecoService.atualizarEndereco(1L, enderecoAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Rua B", resultado.getRua());
        verify(enderecoRepository, times(1)).findById(1L);
        verify(enderecoRepository, times(1)).save(enderecoExistente);
    }

    @Test
    void deveFalharAoAtualizarEnderecoInexistente() {
        // Arrange
        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua B");

        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> enderecoService.atualizarEndereco(1L, enderecoAtualizado));
        assertEquals("Endereço não encontrado", thrown.getMessage());
        verify(enderecoRepository, times(1)).findById(1L);
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setId(1L);

        when(enderecoRepository.findById(1L)).thenReturn(Optional.of(endereco));

        // Act
        enderecoService.deletarEndereco(1L);

        // Assert
        verify(enderecoRepository, times(1)).delete(endereco);
    }

    @Test
    void deveFalharAoDeletarEnderecoInexistente() {
        // Arrange
        when(enderecoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> enderecoService.deletarEndereco(1L));
        assertEquals("Endereço não encontrado", thrown.getMessage());
        verify(enderecoRepository, times(1)).findById(1L);
    }
}
