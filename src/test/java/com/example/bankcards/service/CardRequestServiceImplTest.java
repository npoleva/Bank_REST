package com.example.bankcards.service;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.entity.*;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CardRequestNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardRequestServiceImplTest {

    private CardRequestRepository cardRequestRepository;
    private CardRepository cardRepository;
    private UserRepository userRepository;
    private CardRequestServiceImpl cardRequestService;

    @BeforeEach
    void setUp() {
        cardRequestRepository = mock(CardRequestRepository.class);
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);
        cardRequestService = new CardRequestServiceImpl(cardRequestRepository, cardRepository, userRepository);
    }

    @Test
    void createRequest_cardNotFound_shouldThrowException() {
        CardRequestDto dto = new CardRequestDto();
        dto.setCardId(100L);
        dto.setRequesterId(1L);

        when(cardRepository.findById(100L)).thenReturn(Optional.empty());

        CardNotFoundException ex = assertThrows(CardNotFoundException.class, () -> cardRequestService.createRequest(dto));
        assertEquals("Карта с id 100 не найдена", ex.getMessage());
    }

    @Test
    void createRequest_userNotFound_shouldThrowException() {
        CardRequestDto dto = new CardRequestDto();
        dto.setCardId(10L);
        dto.setRequesterId(2L);

        User user = new User("user1", "hash", "Имя", "Фамилия", "email@example.com", Role.USER);
        user.setId(1L);

        Card card = new Card("encNum", "**** **** **** 1234", user);
        card.setId(10L);

        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> cardRequestService.createRequest(dto));
        assertEquals("Пользователь с id 2 не найден", ex.getMessage());
    }

    @Test
    void updateRequestStatus_requestNotFound_shouldThrowException() {
        when(cardRequestRepository.findById(1L)).thenReturn(Optional.empty());

        CardRequestNotFoundException ex = assertThrows(CardRequestNotFoundException.class,
                () -> cardRequestService.updateRequestStatus(1L, CardRequestStatus.ACCEPTED));
        assertEquals("Заявка не найдена", ex.getMessage());
    }

    @Test
    void existsActiveRequestForCard_shouldReturnTrue() {
        when(cardRequestRepository.existsByCardIdAndRequestStatus(10L, CardRequestStatus.PENDING)).thenReturn(true);

        assertTrue(cardRequestService.existsActiveRequestForCard(10L));
    }

    @Test
    void existsActiveRequestForCard_shouldReturnFalse() {
        when(cardRequestRepository.existsByCardIdAndRequestStatus(10L, CardRequestStatus.PENDING)).thenReturn(false);

        assertFalse(cardRequestService.existsActiveRequestForCard(10L));
    }
}
