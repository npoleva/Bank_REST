package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.entity.CardRequestStatus;
import com.example.bankcards.service.CardRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CardRequestControllerImpl implements CardRequestController {
    private final CardRequestService service;

    @Override
    public CardRequestDto create(@RequestBody CardRequestDto dto) {
        return service.createRequest(dto);
    }

    @Override
    public Page<CardRequestDto> getUserRequests(Long userId, Pageable pageable) {
        return service.getRequestsByRequester(userId, pageable);
    }

    @Override
    public Page<CardRequestDto> getByStatus(String status, Pageable pageable) {
        CardRequestStatus _status = CardRequestStatus.valueOf(status);
        return service.getRequestsByStatus(_status, pageable);
    }

    @Override
    public CardRequestDto updateStatus(Long id, String status) {
        CardRequestStatus _status = CardRequestStatus.valueOf(status);
        return service.updateRequestStatus(id, _status);
    }
}
