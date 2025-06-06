package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardAccessDeniedException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.InvalidAmountException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.CustomUserDetails;
import com.example.bankcards.util.CardNumberGenerator;
import com.example.bankcards.util.EncryptionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final EncryptionUtil encryptionUtil = new EncryptionUtil("MySuperSecretKey");
    private final UserRepository userRepository;

    @Override
    public CardDto createCard(CardDto dto) {
        String plainCardNumber = CardNumberGenerator.generateCardNumber();
        String encrypted = encryptionUtil.encrypt(plainCardNumber);
        String maskedCardNumber = encryptionUtil.mask(encrypted);

        dto.setCardNumberMasked(maskedCardNumber);

        User cardOwner = userRepository.findById(dto.getOwnerId()).orElse(null);

        Card card = CardMapper.toEntity(dto, encrypted, cardOwner);

        cardRepository.save(card);

        return CardMapper.toDto(card);
    }

    @Override
    public CardDto activateCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));
        card.setStatus(CardStatus.ACTIVE);
        cardRepository.save(card);
        return CardMapper.toDto(card);
    }

    @Override
    public CardDto blockCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));
        card.setStatus(CardStatus.BLOCKED);
        cardRepository.save(card);
        return CardMapper.toDto(card);
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    @Override
    public Page<CardDto> getAllCards(Pageable pageable) {
        Page<Card> cards = cardRepository.findAll(pageable);
        return cards.map(CardMapper::toDto);
    }

    @Override
    public Page<CardDto> getUserCards(Long userId, Pageable p) {
        Page<Card> cards = cardRepository.findByOwnerId(userId, p);
        return cards.map(CardMapper::toDto);
    }

    @Override
    public CardDto getCardDetails(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));
        if (!card.getOwner().getId().equals(userId)) {
            throw new SecurityException("Нет доступа к этой карте");
        }
        return CardMapper.toDto(card);
    }

    @Override
    public CardDto changeBalance(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));
        card.setBalance(card.getBalance().add(amount));
        cardRepository.save(card);
        return CardMapper.toDto(card);
    }

    @Transactional
    public CardDto withdrawFromCard(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));

        if (!card.getOwner().getId().equals(getCurrentUserId())) {
            throw new CardAccessDeniedException("Вы не можете управлять этой картой");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Сумма должна быть положительной");
        }

        if (card.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств на карте");
        }

        card.setBalance(card.getBalance().subtract(amount));
        cardRepository.save(card);

        return CardMapper.toDto(card);
    }

    @Transactional
    public CardDto depositToCard(Long cardId, BigDecimal amount) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + cardId + " не найдена"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }

        card.setBalance(card.getBalance().add(amount));

        cardRepository.save(card);

        return CardMapper.toDto(card);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        return userDetails.getId();
    }


    public boolean belongsToUser(Long cardId, Long userId) {
        return cardRepository.existsByIdAndOwnerId(cardId, userId);
    }
}
