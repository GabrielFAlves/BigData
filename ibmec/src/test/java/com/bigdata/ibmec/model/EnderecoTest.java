package com.bigdata.ibmec.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    private Validator validator;

    // Inicializa o Validator antes de cada teste
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveFalharQuandoRuaVazia() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("");  // Rua inválida (vazia)
        endereco.setNumero("123");  // Número válido
        endereco.setBairro("Centro");  // Bairro válido
        endereco.setCidade("São Paulo");  // Cidade válida
        endereco.setEstado("SP");  // Estado válido
        endereco.setCep("12345-678");  // CEP válido

        // Act
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());  // Espera duas violações
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Rua é obrigatória")));
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Rua deve ter entre 3 e 255 caracteres")));
    }


    @Test
    void deveFalharQuandoCepInvalido() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");  // Rua válida
        endereco.setNumero("123");  // Número válido
        endereco.setBairro("Centro");  // Bairro válido
        endereco.setCidade("São Paulo");  // Cidade válida
        endereco.setEstado("SP");  // Estado válido
        endereco.setCep("12345678");  // CEP inválido (sem traço)

        // Act
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("CEP deve seguir o formato XXXXX-XXX", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoEstadoInvalido() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");  // Rua válida
        endereco.setNumero("123");  // Número válido
        endereco.setBairro("Centro");  // Bairro válido
        endereco.setCidade("São Paulo");  // Cidade válida
        endereco.setEstado("Sao Paulo");  // Estado inválido (deve ter 2 caracteres)
        endereco.setCep("12345-678");  // CEP válido

        // Act
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("Estado deve ter 2 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoBairroMuitoCurto() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");  // Rua válida
        endereco.setNumero("123");  // Número válido
        endereco.setBairro("A");  // Bairro muito curto
        endereco.setCidade("São Paulo");  // Cidade válida
        endereco.setEstado("SP");  // Estado válido
        endereco.setCep("12345-678");  // CEP válido

        // Act
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("Bairro deve ter entre 3 e 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void deveFalharQuandoCidadeMuitoCurta() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");  // Rua válida
        endereco.setNumero("123");  // Número válido
        endereco.setBairro("Centro");  // Bairro válido
        endereco.setCidade("A");  // Cidade muito curta
        endereco.setEstado("SP");  // Estado válido
        endereco.setCep("12345-678");  // CEP válido

        // Act
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());  // Espera uma única violação
        assertEquals("Cidade deve ter entre 2 e 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void devePassarComDadosValidos() {
        // Arrange
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");  // Rua válida
        endereco.setNumero("123");  // Número válido
        endereco.setBairro("Centro");  // Bairro válido
        endereco.setCidade("São Paulo");  // Cidade válida
        endereco.setEstado("SP");  // Estado válido
        endereco.setCep("12345-678");  // CEP válido

        // Act
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);

        // Assert
        assertTrue(violations.isEmpty());  // Sem violações para dados válidos
    }
}
