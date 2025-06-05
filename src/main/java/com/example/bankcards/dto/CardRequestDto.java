package com.example.bankcards.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequestDto {
    private Long id;
    private Long cardId;
    private Long requesterId;
    private String requestType;
    private String requestStatus;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
