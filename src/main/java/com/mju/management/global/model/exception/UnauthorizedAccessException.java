package com.mju.management.global.model.exception;

public class UnauthorizedAccessException extends RuntimeException{

    private final ExceptionList exceptionList;

    public UnauthorizedAccessException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
