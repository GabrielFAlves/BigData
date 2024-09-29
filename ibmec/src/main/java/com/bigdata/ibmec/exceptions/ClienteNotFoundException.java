package com.bigdata.ibmec.exceptions;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente com ID " + id + " não encontrado");
    }
}
