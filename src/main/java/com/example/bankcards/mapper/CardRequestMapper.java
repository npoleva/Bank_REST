package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.CardRequestStatus;
import com.example.bankcards.entity.CardRequestType;
import com.example.bankcards.entity.User;

public class CardRequestMapper {

    public static CardRequest toEntity(CardRequestDto dto, Card card, User requester) {
        CardRequest cardRequest = new CardRequest(
                card,
                requester,
                CardRequestType.valueOf(dto.getRequestType()),
                CardRequestStatus.valueOf(dto.getRequestStatus()),
                dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now()
        );

        cardRequest.setComment(dto.getComment());
        cardRequest.setUpdatedAt(dto.getUpdatedAt());
        cardRequest.setId(dto.getId());

        return cardRequest;
    }

    public static CardRequestDto toDto(CardRequest entity) {
        CardRequestDto dto = new CardRequestDto();

        dto.setId(entity.getId());
        dto.setCardId(entity.getCard().getId());
        dto.setRequesterId(entity.getRequester().getId());
        dto.setRequestType(entity.getRequestType().name());
        dto.setRequestStatus(entity.getRequestStatus().name());
        dto.setComment(entity.getComment());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
