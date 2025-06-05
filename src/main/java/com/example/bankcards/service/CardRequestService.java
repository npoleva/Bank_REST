package com.example.bankcards.service;


import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.entity.CardRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRequestService {
    CardRequestDto createRequest(CardRequestDto dto);
    Page<CardRequestDto> getAllRequests(Pageable pageable);
    Page<CardRequestDto> getRequestsByStatus(CardRequestStatus status, Pageable pageable);
    Page<CardRequestDto> getRequestsByRequester(Long requesterId, Pageable pageable);
    CardRequestDto updateRequestStatus(Long requestId, CardRequestStatus newStatus);
    boolean existsActiveRequestForCard(Long cardId);
}
