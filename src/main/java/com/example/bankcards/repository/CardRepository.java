package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findByOwnerId(Long id, Pageable pageable);
    Page<Card> findByOwner_IdAndStatus(Long ownerId, CardStatus status, Pageable pageable);
    boolean existsByIdAndOwnerId(Long cardId, Long ownerId);
}
