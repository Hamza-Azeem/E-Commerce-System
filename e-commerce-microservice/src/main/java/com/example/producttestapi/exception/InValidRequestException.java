package com.example.producttestapi.exception;

public class InValidRequestException extends RuntimeException {
    public InValidRequestException(String message) {
        super(message);
    }
}
