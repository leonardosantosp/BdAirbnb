package com.example.airbnb.exception;

public class PlaceToBookNotFoundException extends RuntimeException{
    public PlaceToBookNotFoundException(String message){
        super(message);
    }
}
