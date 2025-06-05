package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CardControllerImpl implements CardController {
    private final CardService cardService;
    private final UserService userService;

    @Override
    public CardDto createCard(CardDto cardDto) {
        return cardService.createCard(cardDto);
    }

    @Override
    public CardDto activateCard(Long id) {
        return cardService.activateCard(id);
    }

    @Override
    public CardDto blockCard(Long id) {
        return cardService.blockCard(id);
    }

    @Override
    public void deleteCard(Long id) {
        cardService.deleteCard(id);
    }

    @Override
    public Page<CardDto> getAllCards(Pageable pageable) {
        return cardService.getAllCards(pageable);
    }

    @Override
    public Page<CardDto> getUserCards(
            @PathVariable Long userId,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return cardService.getUserCards(userId, pageable);
    }

    @Override
    public CardDto getCardDetails(
            @PathVariable Long cardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return cardService.getCardDetails(cardId, userDetails.getId());
    }

    @Override
    public CardDto changeBalance(Long cardId, BigDecimal amount) {
        return cardService.changeBalance(cardId, amount);
    }
}
