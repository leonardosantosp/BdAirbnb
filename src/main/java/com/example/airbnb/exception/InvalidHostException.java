package com.example.airbnb.exception;

public class InvalidHostException extends RuntimeException{
    public InvalidHostException(String message){
        super(message);
    }
}
