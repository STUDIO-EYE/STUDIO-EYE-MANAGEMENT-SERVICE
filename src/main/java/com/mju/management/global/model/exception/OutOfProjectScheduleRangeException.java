package com.mju.management.global.model.exception;

public class OutOfProjectScheduleRangeException extends RuntimeException{

    private final ExceptionList exceptionList;

    public OutOfProjectScheduleRangeException(ExceptionList exceptionList){
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList(){
        return exceptionList;
    }
}
