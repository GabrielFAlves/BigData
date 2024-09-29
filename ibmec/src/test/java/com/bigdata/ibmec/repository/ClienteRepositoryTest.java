package com.bigdata.ibmec.repository;

import com.bigdata.ibmec.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@gmail.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Maior de 18 anos
        cliente.setTelefone("(11) 91234-5678");

        clienteRepository.save(cliente);
    }

    @Test
    void deveEncontrarClientePorCpf() {
        // Act
        Optional<Cliente> clienteEncontrado = clienteRepository.findByCpf("123.456.789-00");

        // Assert
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("João Silva", clienteEncontrado.get().getNome());
        assertEquals("123.456.789-00", clienteEncontrado.get().getCpf());
    }

    @Test
    void deveFalharAoBuscarClienteComCpfInexistente() {
        // Act
        Optional<Cliente> clienteInexistente = clienteRepository.findByCpf("987.654.321-00");

        // Assert
        assertTrue(clienteInexistente.isEmpty());
    }

    @Test
    void deveEncontrarClientePorEmail() {
        // Act
        Optional<Cliente> clienteEncontrado = clienteRepository.findByEmail("joao.silva@gmail.com");

        // Assert
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("João Silva", clienteEncontrado.get().getNome());
        assertEquals("joao.silva@gmail.com", clienteEncontrado.get().getEmail());
    }

    @Test
    void deveFalharAoBuscarClienteComEmailInexistente() {
        // Act
        Optional<Cliente> clienteInexistente = clienteRepository.findByEmail("email@inexistente.com");

        // Assert
        assertTrue(clienteInexistente.isEmpty());
    }

    @Test
    void deveSalvarClienteComSucesso() {
        // Arrange
        Cliente novoCliente = new Cliente();
        novoCliente.setNome("Maria Oliveira");
        novoCliente.setEmail("maria.oliveira@gmail.com");
        novoCliente.setCpf("321.654.987-00");
        novoCliente.setDataNascimento(LocalDate.of(1995, 5, 15));  // Maior de 18 anos
        novoCliente.setTelefone("(21) 91234-5678");

        // Act
        Cliente clienteSalvo = clienteRepository.save(novoCliente);

        // Assert
        assertNotNull(clienteSalvo.getId());
        assertEquals("Maria Oliveira", clienteSalvo.getNome());
        assertEquals("maria.oliveira@gmail.com", clienteSalvo.getEmail());
    }
}
