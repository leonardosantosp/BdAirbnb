package com.example.airbnb.exception;

public class HostNotFoundException extends RuntimeException{
    public  HostNotFoundException(String message){
        super(message);
    }
}
