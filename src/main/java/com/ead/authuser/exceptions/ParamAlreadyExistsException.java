package com.ead.authuser.exceptions;

public class ParamAlreadyExistsException extends RuntimeException{
    public ParamAlreadyExistsException(String message) {
        super(message);
    }
}
