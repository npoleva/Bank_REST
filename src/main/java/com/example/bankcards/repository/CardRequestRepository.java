package com.example.bankcards.repository;

import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.CardRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {
    Page<CardRequest> findByRequestStatus(CardRequestStatus status, Pageable pageable);
    Page<CardRequest> findByRequesterId(Long requesterId, Pageable pageable);
    boolean existsByCardIdAndRequestStatus(Long cardId, CardRequestStatus status);
}
