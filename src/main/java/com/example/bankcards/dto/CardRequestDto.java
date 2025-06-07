package com.example.bankcards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private Long cardId;
    private Long requesterId;

    @Schema(description = "BLOCK/ACTIVATE")
    private String requestType;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String requestStatus;
    private String comment;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
