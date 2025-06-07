package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/card-requests")
@Tag(name = "Запросы на блокировку/разблокировку карты")
@SecurityRequirement(name = "bearerAuth")
public interface CardRequestController {
    @PostMapping
    @PreAuthorize("@cardRequestServiceImpl.isValidRequest(#dto)")
    @Operation(summary = "Создать запрос на блокировку/разблокировку карты", description = "Возвращает созданный запрос")
    CardRequestDto create(
            @RequestBody CardRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    );

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить все запросы", description = "Доступно только для администратора")
    Page<CardRequestDto> getUserRequests(@PathVariable Long userId, Pageable pageable);

    @GetMapping("/by-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Получить все запросы с определенным статусом",
            description = "Доступно только для администраторов"
    )
    Page<CardRequestDto> getByStatus(@RequestParam String status, Pageable pageable);

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Обновить статус запроса"
    )
    CardRequestDto updateStatus(@PathVariable Long id, @RequestParam String status);
}
