package com.example.airbnb.exception;

public class InvalidBookingException extends RuntimeException{
    public InvalidBookingException(String message){
        super(message);
    }
}
