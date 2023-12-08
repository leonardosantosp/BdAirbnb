package com.example.airbnb.exception;

public class InvalidPlaceException extends RuntimeException {
    public InvalidPlaceException (String message)
    {
        super(message);
    }
}
