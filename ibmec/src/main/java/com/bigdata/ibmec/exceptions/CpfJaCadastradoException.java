package com.bigdata.ibmec.exceptions;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException() {
        super("CPF já cadastrado");
    }

    public CpfJaCadastradoException(String message) {
        super(message);
    }
}
