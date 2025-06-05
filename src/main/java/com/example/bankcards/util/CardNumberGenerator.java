package com.example.bankcards.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CardNumberGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        return cardNumber.toString();
    }
}
