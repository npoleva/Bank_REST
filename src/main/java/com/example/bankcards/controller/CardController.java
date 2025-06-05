package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/api/cards")
public interface CardController {
    @PostMapping("/create")
    CardDto createCard(@RequestBody CardDto cardDto);

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    CardDto activateCard(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/block")
    CardDto blockCard(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    void deleteCard(@PathVariable Long id);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    Page<CardDto> getAllCards(Pageable pageable);

    @GetMapping("/user/{userId}")
    @PreAuthorize("#userId == #userDetails.id")
    Page<CardDto> getUserCards(
            @PathVariable Long userId,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @GetMapping("/{cardId}")
    @PreAuthorize("@cardServiceImpl.belongsToUser(#cardId, #userDetails.id)")
    CardDto getCardDetails(
            @PathVariable Long cardId,
            @AuthenticationPrincipal CustomUserDetails userDetails);

    @PatchMapping("/{cardId}/balance")
    CardDto changeBalance(@PathVariable Long cardId, @RequestParam BigDecimal amount);
}
