package com.bigdata.ibmec.repository;

import com.bigdata.ibmec.model.Cliente;
import com.bigdata.ibmec.model.Endereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnderecoRepositoryTest {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;
    private Endereco endereco;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(11) 91234-5678");

        endereco = new Endereco();
        endereco.setRua("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("01000-000");
        endereco.setCliente(cliente);

        clienteRepository.save(cliente);
        enderecoRepository.save(endereco);
    }

    @Test
    void deveSalvarEnderecoComSucesso() {
        // Arrange
        Endereco novoEndereco = new Endereco();
        novoEndereco.setRua("Rua B");
        novoEndereco.setNumero("456");
        novoEndereco.setBairro("Bairro B");
        novoEndereco.setCidade("Rio de Janeiro");
        novoEndereco.setEstado("RJ");
        novoEndereco.setCep("22000-000");
        novoEndereco.setCliente(cliente);

        // Act
        Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);

        // Assert
        assertNotNull(enderecoSalvo.getId());
        assertEquals("Rua B", enderecoSalvo.getRua());
        assertEquals("Rio de Janeiro", enderecoSalvo.getCidade());
    }

    @Test
    void deveBuscarEnderecoPorIdComSucesso() {
        // Act
        Optional<Endereco> enderecoEncontrado = enderecoRepository.findById(endereco.getId());

        // Assert
        assertTrue(enderecoEncontrado.isPresent());
        assertEquals("Rua A", enderecoEncontrado.get().getRua());
    }

    @Test
    void deveFalharAoBuscarEnderecoInexistente() {
        // Act
        Optional<Endereco> enderecoInexistente = enderecoRepository.findById(999L);

        // Assert
        assertTrue(enderecoInexistente.isEmpty());
    }

    @Test
    void deveListarTodosOsEnderecos() {
        // Act
        List<Endereco> enderecos = enderecoRepository.findAll();

        // Assert
        assertFalse(enderecos.isEmpty());
        assertEquals(1, enderecos.size());  // Esperamos que tenha 1 endereço cadastrado
    }

    @Test
    void deveDeletarEnderecoComSucesso() {
        // Act
        enderecoRepository.deleteById(endereco.getId());

        // Assert
        Optional<Endereco> enderecoDeletado = enderecoRepository.findById(endereco.getId());
        assertTrue(enderecoDeletado.isEmpty());
    }
}
