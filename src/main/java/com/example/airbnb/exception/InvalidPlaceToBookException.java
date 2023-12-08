package com.example.airbnb.exception;

public class InvalidPlaceToBookException extends RuntimeException{
    public InvalidPlaceToBookException(String message){
        super(message);
    }
}
