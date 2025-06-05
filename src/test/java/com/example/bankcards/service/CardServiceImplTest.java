package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    private CardRepository cardRepository;
    private UserRepository userRepository;
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        cardRepository = mock(CardRepository.class);
        userRepository = mock(UserRepository.class);
        cardService = new CardServiceImpl(cardRepository, userRepository);
    }

    @Test
    void createCard_shouldEncryptAndSaveCard() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setId(1L);

        CardDto dto = new CardDto();
        dto.setOwnerId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CardDto created = cardService.createCard(dto);

        assertNotNull(created.getCardNumberMasked());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void activateCard_shouldSetStatusActive() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setId(1L);
        card.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        CardDto result = cardService.activateCard(1L);

        assertEquals(CardStatus.ACTIVE, card.getStatus());
        assertNotNull(result);
    }

    @Test
    void activateCard_cardNotFound_shouldThrow() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        CardNotFoundException ex = assertThrows(CardNotFoundException.class,
                () -> cardService.activateCard(1L));
        assertEquals("Карта с id 1 не найдена", ex.getMessage());
    }

    @Test
    void blockCard_shouldSetStatusBlocked() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setStatus(CardStatus.ACTIVE);
        card.setId(2L);

        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        CardDto result = cardService.blockCard(2L);

        assertEquals(CardStatus.BLOCKED, card.getStatus());
        assertNotNull(result);
    }

    @Test
    void deleteCard_shouldCallRepositoryDelete() {
        doNothing().when(cardRepository).deleteById(5L);
        cardService.deleteCard(5L);
        verify(cardRepository).deleteById(5L);
    }

    @Test
    void getAllCards_shouldReturnPageOfDtos() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setStatus(CardStatus.ACTIVE);
        card.setId(2L);
        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CardDto> result = cardService.getAllCards(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getUserCards_shouldReturnPage() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setStatus(CardStatus.ACTIVE);
        card.setId(2L);
        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardRepository.findByOwnerIdAndStatus(1L, CardStatus.ACTIVE, Pageable.unpaged()))
                .thenReturn(page);

        Page<CardDto> result = cardService.getUserCards(1L, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getCardDetails_ownerMismatch_shouldThrowSecurityException() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setStatus(CardStatus.ACTIVE);
        card.setId(2L);

        when(cardRepository.findById(3L)).thenReturn(Optional.of(card));

        SecurityException ex = assertThrows(SecurityException.class,
                () -> cardService.getCardDetails(3L, 5L));
        assertEquals("Нет доступа к этой карте", ex.getMessage());
    }

    @Test
    void changeBalance_shouldAddAmount() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setStatus(CardStatus.ACTIVE);
        card.setId(2L);
        card.setBalance(BigDecimal.valueOf(100));
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        CardDto dto = cardService.changeBalance(1L, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), card.getBalance());
        assertNotNull(dto);
    }

    @Test
    void changeBalance_cardNotFound_shouldThrow() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        CardNotFoundException ex = assertThrows(CardNotFoundException.class,
                () -> cardService.changeBalance(1L, BigDecimal.ONE));
        assertEquals("Карта с id 1 не найдена", ex.getMessage());
    }


    @Test
    void withdrawFromCard_success() {
        User user = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        user.setId(1L);

        Card card = new Card(
                "encrypted-1234567890123456",
                "**** **** **** 3456",
                user
        );
        card.setBalance(BigDecimal.valueOf(200));
        card.setId(2L);

        when(cardRepository.findById(2L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        setupSecurityContext(1L);

        CardDto dto = cardService.withdrawFromCard(2L, BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), card.getBalance());
        assertNotNull(dto);
    }

    @Test
    void withdrawFromCard_notOwner_shouldThrowAccessDenied() {
        User owner = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        owner.setId(2L);
        Card card = new Card("encrypted-1234567890123456",
                "**** **** **** 3456",
                owner);
        card.setBalance(BigDecimal.valueOf(1000));
        card.setId(10L);

        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));

        setupSecurityContext(1L);

        assertThrows(com.example.bankcards.exception.CardAccessDeniedException.class,
                () -> cardService.withdrawFromCard(10L, BigDecimal.valueOf(10)));
    }

    @Test
    void withdrawFromCard_cardNotFound_shouldThrow() {
        when(cardRepository.findById(10L)).thenReturn(Optional.empty());
        setupSecurityContext(1L);

        CardNotFoundException ex = assertThrows(CardNotFoundException.class,
                () -> cardService.withdrawFromCard(10L, BigDecimal.TEN));

        assertEquals("Карта с id 10 не найдена", ex.getMessage());
    }

    @Test
    void depositToCard_success() {
        User owner = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        owner.setId(2L);
        Card card = new Card("encrypted-1234567890123456",
                "**** **** **** 3456",
                owner);
        card.setBalance(BigDecimal.valueOf(1000));
        card.setId(10L);
        card.setBalance(BigDecimal.valueOf(100));
        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        CardDto dto = cardService.depositToCard(10L, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), card.getBalance());
        assertNotNull(dto);
    }

    @Test
    void depositToCard_negativeAmount_shouldThrow() {
        User owner = new User("user", "hash", "Имя", "Фамилия", "email@mail.com", Role.USER);
        owner.setId(2L);
        Card card = new Card("encrypted-1234567890123456",
                "**** **** **** 3456",
                owner);
        card.setBalance(BigDecimal.valueOf(1000));
        card.setId(10L);
        card.setBalance(BigDecimal.valueOf(100));
        when(cardRepository.findById(10L)).thenReturn(Optional.of(card));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cardService.depositToCard(10L, BigDecimal.valueOf(-10)));

        assertEquals("Сумма должна быть положительной", ex.getMessage());
    }

    @Test
    void belongsToUser_shouldReturnTrueOrFalse() {
        when(cardRepository.existsByIdAndOwnerId(1L, 2L)).thenReturn(true);
        assertTrue(cardService.belongsToUser(1L, 2L));

        when(cardRepository.existsByIdAndOwnerId(1L, 2L)).thenReturn(false);
        assertFalse(cardService.belongsToUser(1L, 2L));
    }

    private void setupSecurityContext(Long userId) {
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(userDetails.getId()).thenReturn(userId);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
