package com.example.userservice.repository;

import com.example.userservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
    List<Subscription> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);

    void deleteByUserId(Long userId);
}
