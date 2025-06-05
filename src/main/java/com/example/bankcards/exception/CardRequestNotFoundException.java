package com.example.bankcards.exception;

public class CardRequestNotFoundException extends RuntimeException {
    public CardRequestNotFoundException(String message) {
        super(message);
    }
}
