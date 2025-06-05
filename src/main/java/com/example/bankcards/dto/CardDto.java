package com.example.bankcards.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private Long id;
    private String cardNumberMasked;
    private Long ownerId;
    private LocalDate expirationDate;
    private String status;
    private BigDecimal balance;
}
