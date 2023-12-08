package com.example.airbnb.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message){
        super(message);
    }
}
