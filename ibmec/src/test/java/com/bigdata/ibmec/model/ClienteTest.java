package com.bigdata.ibmec.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Validator validator;

    // Inicializa o Validator antes de cada teste
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveFalharQuandoNomeVazio() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("");  // Nome inválido (vazio)
        cliente.setEmail("joao.silva@gmail.com");  // Email válido
        cliente.setCpf("123.456.789-00");  // CPF válido
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Data válida
        cliente.setTelefone("(11) 91234-5678");  // Telefone válido

        // Act
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Debug: Verifique as violações
        violations.forEach(v -> System.out.println(v.getPropertyPath() + " - " + v.getMessage()));

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());  // Espera duas violações
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Nome é obrigatório")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Nome deve ter entre 3 e 100 caracteres")));
    }



    @Test
    void deveFalharQuandoEmailInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");  // Nome válido
        cliente.setEmail("emailinvalido");  // Email inválido
        cliente.setCpf("123.456.789-00");  // CPF válido
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Data válida
        cliente.setTelefone("(11) 91234-5678");  // Telefone válido

        // Act
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("E-mail deve ser válido", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoCpfInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");  // Nome válido
        cliente.setEmail("joao.silva@gmail.com");  // Email válido
        cliente.setCpf("12345678900");  // CPF inválido (sem pontuação)
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Data válida
        cliente.setTelefone("(11) 91234-5678");  // Telefone válido

        // Act
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("CPF deve seguir o formato XXX.XXX.XXX-XX", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoDataNascimentoFutura() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");  // Nome válido
        cliente.setEmail("joao.silva@gmail.com");  // Email válido
        cliente.setCpf("123.456.789-00");  // CPF válido
        cliente.setDataNascimento(LocalDate.of(2030, 1, 1));  // Data futura (inválida)
        cliente.setTelefone("(11) 91234-5678");  // Telefone válido

        // Act
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("Data de nascimento deve ser uma data passada", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoTelefoneInvalido() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");  // Nome válido
        cliente.setEmail("joao.silva@gmail.com");  // Email válido
        cliente.setCpf("123.456.789-00");  // CPF válido
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Data válida
        cliente.setTelefone("11912345678");  // Telefone inválido (sem formato correto)

        // Act
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("Telefone deve seguir o formato (XX) XXXXX-XXXX", violations.iterator().next().getMessage());
    }

    @Test
    void devePassarComDadosValidos() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");  // Nome válido
        cliente.setEmail("joao.silva@gmail.com");  // Email válido
        cliente.setCpf("123.456.789-00");  // CPF válido
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Data válida
        cliente.setTelefone("(11) 91234-5678");  // Telefone válido

        // Act
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Assert
        assertTrue(violations.isEmpty());  // Sem violações para dados válidos
    }
}
