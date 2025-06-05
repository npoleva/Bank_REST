package com.example.bankcards.repository;

import com.example.bankcards.entity.CardTransfer;
import com.example.bankcards.entity.CardTransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardTransferRepository extends CrudRepository<CardTransfer, Long> {
    Page<CardTransfer> findByInitiatorId(Long initiatorId, Pageable pageable);
    List<CardTransfer> findByToCardId(Long toCardId);
    List<CardTransfer> findByInitiatorIdAndStatus(Long initiatorId, CardTransferStatus status);
    Page<CardTransfer> findByStatus(CardTransferStatus status, Pageable pageable);
}
