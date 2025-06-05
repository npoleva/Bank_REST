package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardTransfer;
import com.example.bankcards.entity.CardTransferStatus;
import com.example.bankcards.entity.User;

public class CardTransferMapper {
    public static CardTransfer toEntity(CardTransferDto dto, Card fromCard, Card toCard, User initiator) {
        CardTransfer transfer = new CardTransfer(
                fromCard,
                toCard,
                dto.getAmount(),
                initiator
        );

        transfer.setComment(dto.getComment());

        return transfer;
    }

    public static CardTransferDto toDto(CardTransfer entity) {
        CardTransferDto dto = new CardTransferDto();

        dto.setId(entity.getId());
        dto.setFromCardId(entity.getFromCard().getId());
        dto.setToCardId(entity.getToCard().getId());
        dto.setAmount(entity.getAmount());
        dto.setInitiatorId(entity.getInitiator().getId());
        dto.setStatus(entity.getStatus().name());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setComment(entity.getComment());

        return dto;
    }
}
