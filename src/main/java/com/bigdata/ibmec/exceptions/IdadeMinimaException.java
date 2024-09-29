package com.bigdata.ibmec.exceptions;

public class IdadeMinimaException extends RuntimeException {
    public IdadeMinimaException() {
        super("O cliente deve ter pelo menos 18 anos.");
    }
}
