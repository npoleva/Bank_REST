package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor()
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "card_number_encrypted", unique = true, nullable = false)
    private String cardNumberEncrypted;

    @NonNull
    @Column(name = "card_number_masked", nullable = false)
    private String cardNumberMasked;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @NonNull
    private User owner;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @PrePersist
    public void prePersist() {
        status = CardStatus.ACTIVE;
        expirationDate = LocalDate.now().plusMonths(5);
        balance = BigDecimal.ZERO;
    }
}
