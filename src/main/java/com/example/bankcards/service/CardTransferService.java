package com.example.bankcards.service;


import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.entity.CardTransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CardTransferService {
    CardTransferDto createTransfer(CardTransferDto dto);

    CardTransferDto getTransferById(Long id);

    Page<CardTransferDto> getTransfersByInitiator(Long initiatorId, Pageable pageable);

    List<CardTransferDto> getTransfersToCard(Long toCardId);

    List<CardTransferDto> getTransfersByInitiatorAndStatus(Long initiatorId, CardTransferStatus status);

    Page<CardTransferDto> getTransfersByStatus(CardTransferStatus status, Pageable pageable);

    CardTransferDto updateTransferStatus(Long transferId, CardTransferStatus newStatus);

    void deleteTransfer(Long transferId);
}
