package com.bigdata.ibmec.service;

import com.bigdata.ibmec.DTO.ClienteComEnderecoDTO;
import com.bigdata.ibmec.exceptions.IdadeMinimaException;
import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import com.bigdata.ibmec.repository.ClienteRepository;
import com.bigdata.ibmec.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAdicionarClienteComEnderecoComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1)); // Maior de 18 anos
        cliente.setEnderecos(new ArrayList<>());

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

        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        // Act
        Cliente clienteSalvo = clienteService.adicionarClienteComEndereco(clienteComEnderecoDTO);

        // Assert
        assertNotNull(clienteSalvo);
        assertEquals("João Silva", clienteSalvo.getNome());
        verify(clienteRepository, times(1)).save(cliente);
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    void deveFalharQuandoClienteForMenorDe18Anos() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setDataNascimento(LocalDate.now().minusYears(17)); // Menor de 18 anos

        Endereco endereco = new Endereco();
        ClienteComEnderecoDTO clienteComEnderecoDTO = new ClienteComEnderecoDTO();
        clienteComEnderecoDTO.setCliente(cliente);
        clienteComEnderecoDTO.setEndereco(endereco);

        // Act & Assert
        assertThrows(IdadeMinimaException.class, () -> clienteService.adicionarClienteComEndereco(clienteComEnderecoDTO));
    }

    @Test
    void deveListarTodosOsClientes() {
        // Arrange
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente());
        when(clienteRepository.findAll()).thenReturn(clientes);

        // Act
        List<Cliente> resultado = clienteService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarClientePorIdComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente clienteEncontrado = clienteService.buscarClientePorId(1L);

        // Assert
        assertNotNull(clienteEncontrado);
        assertEquals(1L, clienteEncontrado.getId());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveFalharQuandoClienteNaoForEncontrado() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> clienteService.buscarClientePorId(1L));
        assertEquals("Cliente não encontrado", thrown.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void deveAtualizarDadosDoClienteComSucesso() {
        // Arrange
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNome("João Silva");

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Atualizado");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteAtualizado);

        // Act
        Cliente clienteSalvo = clienteService.atualizarDadosCliente(1L, clienteAtualizado);

        // Assert
        assertNotNull(clienteSalvo);
        assertEquals("João Atualizado", clienteSalvo.getNome());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(clienteExistente);
    }

    @Test
    void deveDeletarClienteComSucesso() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        clienteService.deletarCliente(1L);

        // Assert
        verify(clienteRepository, times(1)).delete(cliente);
    }

    @Test
    void deveFalharAoDeletarClienteInexistente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> clienteService.deletarCliente(1L));
        assertEquals("Cliente não encontrado", thrown.getMessage());
        verify(clienteRepository, times(1)).findById(1L);
    }
}
