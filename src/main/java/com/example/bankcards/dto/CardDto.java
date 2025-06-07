package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String cardNumberMasked;
    private Long ownerId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate expirationDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String status;
    private BigDecimal balance;
}
