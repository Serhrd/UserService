package com.example.userservice.service;


import com.example.userservice.dto.SubscriptionDto;
import com.example.userservice.dto.SubscriptionRequest;
import com.example.userservice.exception.SubscriptionNotFoundException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.Subscription;
import com.example.userservice.model.User;
import com.example.userservice.repository.SubscriptionRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubscriptionDto createSubscription(Long userId, SubscriptionRequest request) {
        log.info("Creating subscription for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Subscription subscription = Subscription.builder()
                .serviceName(request.getServiceName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .user(user)
                .build();

        subscription = subscriptionRepository.save(subscription);
        log.info("Created subscription with id: {} for user id: {}", subscription.getId(), userId);

        return mapToDto(subscription);
    }

    public List<SubscriptionDto> getSubscriptionsByUserId(Long userId) {
        log.info("Fetching subscriptions for user id: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSubscription(Long userId, Long subscriptionId) {
        log.info("Deleting subscription id: {} for user id: {}", subscriptionId, userId);
        if (!subscriptionRepository.existsByIdAndUserId(subscriptionId, userId)) {
            throw new SubscriptionNotFoundException(subscriptionId);
        }

        subscriptionRepository.deleteById(subscriptionId);
        log.info("Deleted subscription id: {} for user id: {}", subscriptionId, userId);
    }

    @Transactional
    public void deleteAllSubscriptionsForUser(Long userId) {
        log.info("Deleting all subscriptions for user id: {}", userId);
        subscriptionRepository.deleteByUserId(userId);
        log.info("Deleted all subscriptions for user id: {}", userId);
    }

    private SubscriptionDto mapToDto(Subscription subscription) {
        return SubscriptionDto.builder()
                .id(subscription.getId())
                .serviceName(subscription.getServiceName())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .userId(subscription.getUser().getId())
                .build();
    }
}