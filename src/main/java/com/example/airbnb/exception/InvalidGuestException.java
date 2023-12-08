package com.example.airbnb.exception;

public class InvalidGuestException extends RuntimeException{
    public InvalidGuestException(String message){
        super(message);
    }

}
