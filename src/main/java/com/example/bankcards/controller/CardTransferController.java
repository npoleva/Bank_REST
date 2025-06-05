package com.example.bankcards.controller;

import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/transfers")
public interface CardTransferController {
    @PostMapping
    CardTransferDto createTransfer(@RequestBody CardTransferDto dto);

    @GetMapping("/by-initiator/{initiatorId}")
    @PreAuthorize("hasRole('ADMIN')")
    Page<CardTransferDto> getByInitiator(@PathVariable Long initiatorId, Pageable pageable);

    @GetMapping("/by-to-card/{toCardId}")
    @PreAuthorize("hasRole('ADMIN')")
    List<CardTransferDto> getByToCard(@PathVariable Long toCardId);

    @GetMapping("/by-status")
    @PreAuthorize("hasRole('ADMIN')")
    Page<CardTransferDto> getByStatus(@RequestParam String status, Pageable pageable);

    @PutMapping("/{id}/status")
    @PreAuthorize("@cardServiceImpl.belongsToUser(#id, #userDetails.id)")
    CardTransferDto updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
