package com.example.bankcards.service;

import com.example.bankcards.dto.CardTransferDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardTransfer;
import com.example.bankcards.entity.CardTransferStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.*;
import com.example.bankcards.mapper.CardTransferMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardTransferRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardTransferServiceImpl implements CardTransferService {

    private final CardTransferRepository cardTransferRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardService cardService;
    private final CardServiceImpl cardServiceImpl;

    @Override
    public CardTransferDto createTransfer(CardTransferDto dto) {
        Card fromCard = cardRepository.findById(dto.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException("Исходящая карта с id " + dto.getFromCardId() + " не найдена"));

        Card toCard = cardRepository.findById(dto.getToCardId())
                .orElseThrow(() -> new CardNotFoundException("Целевая карта с id " + dto.getToCardId() + " не найдена"));

        User initiator = userRepository.findById(dto.getInitiatorId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + dto.getInitiatorId() + " не найден"));

        if (dto.getFromCardId().equals(dto.getToCardId())) {
            throw new SameCardTransferException("Нельзя перевести средства на ту же самую карту");
        }

        BigDecimal amount = dto.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidTransferAmountException("Сумма перевода не может быть нулевой");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            cardService.withdrawFromCard(fromCard.getId(), amount.abs());
            cardService.depositToCard(toCard.getId(), amount.abs());
            fromCard = cardRepository.findById(fromCard.getId()).get();
            toCard = cardRepository.findById(toCard.getId()).get();
        } else {
            cardService.depositToCard(toCard.getId(), amount);
            cardService.withdrawFromCard(fromCard.getId(), amount);
            toCard = cardRepository.findById(toCard.getId()).get();
            fromCard = cardRepository.findById(fromCard.getId()).get();
        }

        CardTransfer transfer = CardTransferMapper.toEntity(dto, fromCard, toCard, initiator);

        transfer.setStatus(CardTransferStatus.SUCCESS);
        cardTransferRepository.save(transfer);

        return CardTransferMapper.toDto(transfer);
    }

    @Override
    public CardTransferDto getTransferById(Long id) {
        CardTransfer transfer = cardTransferRepository.findById(id)
                .orElseThrow(() -> new TransferNotFoundException("Перевод не найден"));
        return CardTransferMapper.toDto(transfer);
    }

    @Override
    public Page<CardTransferDto> getTransfersByInitiator(Long initiatorId, Pageable pageable) {
        return cardTransferRepository.findByInitiatorId(initiatorId, pageable)
                .map(CardTransferMapper::toDto);
    }

    @Override
    public List<CardTransferDto> getTransfersToCard(Long toCardId) {
        List<CardTransfer> transfers = cardTransferRepository.findByToCardId(toCardId);
        return transfers.stream().map(CardTransferMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardTransferDto> getTransfersByInitiatorAndStatus(Long initiatorId, CardTransferStatus status) {
        List<CardTransfer> transfers = cardTransferRepository.findByInitiatorIdAndStatus(initiatorId, status);
        return transfers.stream().map(CardTransferMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<CardTransferDto> getTransfersByStatus(CardTransferStatus status, Pageable pageable) {
        return cardTransferRepository.findByStatus(status, pageable)
                .map(CardTransferMapper::toDto);
    }

    @Override
    public CardTransferDto updateTransferStatus(Long transferId, CardTransferStatus newStatus) {
        CardTransfer transfer = cardTransferRepository.findById(transferId)
                .orElseThrow(() -> new TransferNotFoundException("Перевод не найден"));

        transfer.setStatus(newStatus);
        cardTransferRepository.save(transfer);

        return CardTransferMapper.toDto(transfer);
    }

    @Override
    public void deleteTransfer(Long transferId) {
        cardTransferRepository.deleteById(transferId);
    }

    public boolean isValidTransfer(CardTransferDto dto) {
        Long fromCardId = dto.getFromCardId();
        Long initiatorId = dto.getInitiatorId();

        return cardServiceImpl.belongsToUser(fromCardId, initiatorId);
    }
}
