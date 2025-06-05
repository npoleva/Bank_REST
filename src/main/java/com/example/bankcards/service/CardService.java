package com.example.bankcards.service;


import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface CardService {
    CardDto createCard(CardDto dto);
    CardDto activateCard(Long cardId);
    CardDto blockCard(Long cardId);
    void deleteCard(Long cardId);
    Page<CardDto> getAllCards(Pageable pageable);
    Page<CardDto> getUserCards(Long userId, Pageable p);
    CardDto getCardDetails(Long cardId, Long userId);
    CardDto changeBalance(Long cardId, BigDecimal amount);
    CardDto depositToCard(Long cardId, BigDecimal amount);
    CardDto withdrawFromCard(Long cardId, BigDecimal amount);
}
