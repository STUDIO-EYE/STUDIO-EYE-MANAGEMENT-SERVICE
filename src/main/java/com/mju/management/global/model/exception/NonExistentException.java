package com.mju.management.global.model.exception;

public class NonExistentException extends RuntimeException {
    private final ExceptionList exceptionList;

    public NonExistentException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
