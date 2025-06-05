package com.example.bankcards.controller;

import com.example.bankcards.dto.CardRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/card-requests")
public interface CardRequestController {
    @PostMapping
    CardRequestDto create(@RequestBody CardRequestDto dto);

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    Page<CardRequestDto> getUserRequests(@PathVariable Long userId, Pageable pageable);

    @GetMapping("/by-status")
    @PreAuthorize("hasRole('ADMIN')")
    Page<CardRequestDto> getByStatus(@RequestParam String status, Pageable pageable);

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    CardRequestDto updateStatus(@PathVariable Long id, @RequestParam String status);
}
