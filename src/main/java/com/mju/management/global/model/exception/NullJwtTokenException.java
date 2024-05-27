package com.mju.management.global.model.exception;

public class NullJwtTokenException extends RuntimeException{
    private final ExceptionList exceptionList;

    public NullJwtTokenException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
