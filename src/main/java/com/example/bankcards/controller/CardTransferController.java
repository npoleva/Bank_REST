package com.example.bankcards.controller;

import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/transfers")
@Tag(name = "Переводы с карты на карту")
@SecurityRequirement(name = "bearerAuth")
public interface CardTransferController {
    @PostMapping
    @Operation(
            summary = "Создать перевод с карты на карту",
            description = "Возвращает созданный перевод с обновленным статусом"
    )
    CardTransferDto createTransfer(@RequestBody CardTransferDto dto);

    @GetMapping("/by-initiator/{initiatorId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти перевод по инициатору",
            description = "Доступно только для администратора"
    )
    Page<CardTransferDto> getByInitiator(@PathVariable Long initiatorId, Pageable pageable);

    @GetMapping("/by-to-card/{toCardId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти перевод по карте, на которую переводили",
            description = "Доступно только для администратора"
    )
    List<CardTransferDto> getByToCard(@PathVariable Long toCardId);

    @GetMapping("/by-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Найти перевод по статусу",
            description = "Доступно только для администратора"
    )
    Page<CardTransferDto> getByStatus(@RequestParam String status, Pageable pageable);

    @PutMapping("/{id}/status")
    @PreAuthorize("@cardServiceImpl.belongsToUser(#id, #userDetails.id)")
    @Operation(
            summary = "Обновить статус перевода",
            description = "Доступно только для администратора"
    )
    CardTransferDto updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal CustomUserDetails userDetails);
}
