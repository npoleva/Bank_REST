package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Карты", description = "Управление банковскими картами")
@RequestMapping("/api/cards")
@SecurityRequirement(name = "bearerAuth")
public interface CardController {
    @PostMapping("/create")
    @Operation(summary = "Создать новую карту", description = "Возвращает созданную карту")
    CardDto createCard(@RequestBody CardDto cardDto);

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Отправить запрос на активацию карту", description = "Возвращает активированную карту")
    CardDto activateCard(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/block")
    @Operation(summary = "Отправить запрос на блокировку карты", description = "Возвращает заблокированную карту")
    CardDto blockCard(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить карту",
            description = "Доступно только администратору"
    )
    void deleteCard(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(
            summary = "Получить все карты",
            description = "Доступно только администратору"
    )
    Page<CardDto> getAllCards(Pageable pageable);

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == #userDetails.id")
    @Operation(
            summary = "Получить все свои карты"
    )
    Page<CardDto> getUserCards(
            @PathVariable Long userId,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @GetMapping("/{cardId}")
    @PreAuthorize("@cardServiceImpl.belongsToUser(#cardId, #userDetails.id)")
    @Operation(
            summary = "Получить данные карты",
            description = "Доступно только владельцу карты"
    )
    CardDto getCardDetails(
            @PathVariable Long cardId,
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @PatchMapping("/{cardId}/balance")
    @Operation(
            summary = "Изменить баланс карты"
    )
    CardDto changeBalance(@PathVariable Long cardId, @RequestParam BigDecimal amount);
}
