package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CardMapper {
    public static CardDto toDto(Card card) {
        if (card == null) {
            return null;
        }
        CardDto dto = new CardDto();

        dto.setId(card.getId());
        dto.setCardNumberMasked(card.getCardNumberMasked());
        dto.setOwnerId(card.getOwner() != null ? card.getOwner().getId() : null);
        dto.setExpirationDate(card.getExpirationDate());
        dto.setStatus(card.getStatus() != null ? card.getStatus().name() : null);
        dto.setBalance(card.getBalance());
        return dto;
    }

    public static Card toEntity(CardDto dto, String cardNumberEncrypted, User owner) {
        if (dto == null || cardNumberEncrypted == null || owner == null) {
            throw new IllegalArgumentException("DTO, cardNumberEncrypted and owner must not be null");
        }

        Card card = new Card(
                cardNumberEncrypted,
                dto.getCardNumberMasked(),
                owner
        );

        card.setExpirationDate(dto.getExpirationDate() != null
                ? dto.getExpirationDate()
                : LocalDate.now().plusYears(4));

        card.setStatus(dto.getStatus() != null
                ? CardStatus.valueOf(dto.getStatus())
                : CardStatus.ACTIVE);

        card.setBalance(dto.getBalance() != null
                ? dto.getBalance()
                : BigDecimal.valueOf(0));
        if (dto.getId() != null) {
            card.setId(dto.getId());
        }
        return card;
    }
}
