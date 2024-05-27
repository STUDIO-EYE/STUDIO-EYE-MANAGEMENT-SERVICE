package com.mju.management.global.model.exception;

public class UserNotFindException extends RuntimeException{

    private final ExceptionList exceptionList;

    public UserNotFindException(ExceptionList exceptionList){
        super(exceptionList.getMessage());
        this.exceptionList = exceptionList;
    }

    public ExceptionList getExceptionList(){
        return exceptionList;
    }
}
