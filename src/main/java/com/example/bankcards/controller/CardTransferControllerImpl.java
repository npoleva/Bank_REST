package com.example.bankcards.controller;

import com.example.bankcards.controller.CardTransferController;
import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.entity.CardTransferStatus;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.service.CardTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardTransferControllerImpl implements CardTransferController {
    private final CardTransferService service;

    @Override
    public CardTransferDto createTransfer(@RequestBody CardTransferDto dto) {
        return service.createTransfer(dto);
    }

    @Override
    public Page<CardTransferDto> getByInitiator(Long initiatorId, Pageable pageable) {
        return service.getTransfersByInitiator(initiatorId, pageable);
    }

    @Override
    public List<CardTransferDto> getByToCard(Long toCardId) {
        return service.getTransfersToCard(toCardId);
    }

    @Override
    public Page<CardTransferDto> getByStatus(String status, Pageable pageable) {
        CardTransferStatus _status = CardTransferStatus.valueOf(status);
        return service.getTransfersByStatus(_status, pageable);
    }

    @Override
    public CardTransferDto updateStatus(Long id, String status, CustomUserDetails userDetails) {
        CardTransferStatus _status = CardTransferStatus.valueOf(status);
        return service.updateTransferStatus(id, _status);
    }
}
