package com.bigdata.ibmec.exceptions;

public class EnderecoNotFoundException extends RuntimeException {
    public EnderecoNotFoundException(Long id) {
        super("Endereço com ID " + id + " não encontrado");
    }
}
