package com.mju.management.global.model.Exception;

public class InvalidDateFormatException extends RuntimeException{
    private final ExceptionList exceptionList;

    public InvalidDateFormatException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
