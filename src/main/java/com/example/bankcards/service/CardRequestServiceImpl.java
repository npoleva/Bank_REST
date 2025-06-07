package com.example.bankcards.service;


import com.example.bankcards.dto.CardRequestDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.CardRequestStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CardRequestNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mapper.CardRequestMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardRequestRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardRequestServiceImpl implements CardRequestService {
    private final CardRequestRepository cardRequestRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardServiceImpl cardServiceImpl;

    @Override
    public CardRequestDto createRequest(CardRequestDto dto) {
        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Карта с id " + dto.getCardId() + " не найдена"));

        User requester = userRepository.findById(dto.getRequesterId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + dto.getRequesterId() + " не найден"));

        CardRequest cardRequest = CardRequestMapper.toEntity(dto, card, requester);

        if (cardRequest.getRequestStatus() == null) {
            cardRequest.setRequestStatus(CardRequestStatus.PENDING);
        }

        cardRequestRepository.save(cardRequest);

        return CardRequestMapper.toDto(cardRequest);
    }

    @Override
    public Page<CardRequestDto> getAllRequests(Pageable pageable) {
        return cardRequestRepository.findAll(pageable).map(CardRequestMapper::toDto);
    }

    @Override
    public Page<CardRequestDto> getRequestsByStatus(CardRequestStatus status, Pageable pageable) {
        return cardRequestRepository.findByRequestStatus(status, pageable).map(CardRequestMapper::toDto);
    }

    @Override
    public Page<CardRequestDto> getRequestsByRequester(Long requesterId, Pageable pageable) {
        return cardRequestRepository.findByRequesterId(requesterId, pageable).map(CardRequestMapper::toDto);
    }

    @Override
    public CardRequestDto updateRequestStatus(Long requestId, CardRequestStatus newStatus) {
        CardRequest request = cardRequestRepository.findById(requestId)
                .orElseThrow(() -> new CardRequestNotFoundException("Заявка не найдена"));

        request.setRequestStatus(newStatus);
        cardRequestRepository.save(request);

        return CardRequestMapper.toDto(request);
    }

    @Override
    public boolean existsActiveRequestForCard(Long cardId) {
        return cardRequestRepository.existsByCardIdAndRequestStatus(cardId, CardRequestStatus.PENDING);
    }

    public boolean isValidRequest(CardRequestDto dto) {
        Long cardId = dto.getCardId();
        Long requesterId = dto.getRequesterId();

        return cardServiceImpl.belongsToUser(cardId, requesterId);
    }
}
