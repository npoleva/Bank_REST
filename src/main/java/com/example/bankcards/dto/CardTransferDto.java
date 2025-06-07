package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTransferDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private Long fromCardId;
    private Long toCardId;
    private BigDecimal amount;
    private Long initiatorId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String status;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    private String comment;
}
