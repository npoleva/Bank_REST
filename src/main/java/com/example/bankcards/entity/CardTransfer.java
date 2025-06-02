package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "card_transfers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CardTransfer {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card_id", nullable = false)
    private Card fromCard;

    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card_id", nullable = false)
    private Card toCard;

    @NonNull
    @Column(nullable = false)
    private BigDecimal amount;

    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardTransferStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 1000)
    private String comment;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
