package com.example.bankcards.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTransferDto {
    private Long id;
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
    private Long initiatorId;
    private String status;
    private LocalDateTime createdAt;
    private String comment;
}
