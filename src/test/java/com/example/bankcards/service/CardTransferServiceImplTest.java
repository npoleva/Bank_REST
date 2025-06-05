package com.example.bankcards.service;

import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InvalidTransferAmountException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardTransferServiceImplTest {

    private CardRepository cardRepository;
    private CardTransferRepository cardTransferRepository;
    private UserRepository userRepository;
    private CardService cardService;
    private CardTransferServiceImpl cardTransferService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        cardTransferRepository = mock(CardTransferRepository.class);
        userRepository = mock(UserRepository.class);
        cardService = mock(CardService.class);
        cardTransferService = new CardTransferServiceImpl(cardTransferRepository, cardRepository, userRepository, cardService);
    }

    @Test
    void createTransfer_successfulPositiveAmount_shouldReturnDtoWithSuccessStatus() {
        User user = new User(
                "testuser",
                "hashed_password_123",
                "Иван",
                "Иванов",
                "ivan.ivanov@example.com",
                Role.USER
        );

        user.setId(1L);

        Card fromCard = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        fromCard.setId(1L);
        fromCard.setBalance(BigDecimal.valueOf(500));

        Card toCard = new Card(
                "encrypted-1234567890123457",
                "**** **** **** 3457",
                user
        );
        toCard.setId(2L);
        toCard.setBalance(BigDecimal.valueOf(300));

        CardTransferDto dto = new CardTransferDto();
        dto.setFromCardId(1L);
        dto.setToCardId(2L);
        dto.setInitiatorId(1L);
        dto.setAmount(BigDecimal.valueOf(100));
        dto.setComment("Test comment");

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        CardTransferDto result = cardTransferService.createTransfer(dto);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals("Test comment", result.getComment());
    }

    @Test
    void createTransfer_zeroAmount_shouldThrowException() {
        User user = new User(
                "testuser",
                "hashed_password_123",
                "Иван",
                "Иванов",
                "ivan.ivanov@example.com",
                Role.USER
        );

        user.setId(1L);

        Card fromCard = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        fromCard.setId(1L);
        fromCard.setBalance(BigDecimal.valueOf(500));

        Card toCard = new Card(
                "encrypted-1234567890123457",
                "**** **** **** 3457",
                user
        );
        toCard.setId(2L);
        toCard.setBalance(BigDecimal.valueOf(300));

        CardTransferDto dto = new CardTransferDto();
        dto.setFromCardId(1L);
        dto.setToCardId(2L);
        dto.setInitiatorId(1L);
        dto.setAmount(BigDecimal.ZERO);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        InvalidTransferAmountException exception = assertThrows(InvalidTransferAmountException.class, () -> cardTransferService.createTransfer(dto));
        assertEquals("Сумма перевода не может быть нулевой", exception.getMessage());
    }

    @Test
    void createTransfer_cardNotFound_shouldThrowCardNotFoundException() {
        CardTransferDto dto = new CardTransferDto();
        dto.setFromCardId(100L);
        dto.setToCardId(200L);
        dto.setInitiatorId(1L);
        dto.setAmount(BigDecimal.TEN);

        when(cardRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardTransferService.createTransfer(dto));
    }
}
