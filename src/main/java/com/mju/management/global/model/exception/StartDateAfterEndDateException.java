package com.mju.management.global.model.exception;

public class StartDateAfterEndDateException extends RuntimeException{
    private final ExceptionList exceptionList;

    public StartDateAfterEndDateException(ExceptionList exceptionList) {
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList() {
        return exceptionList;
    }
}
