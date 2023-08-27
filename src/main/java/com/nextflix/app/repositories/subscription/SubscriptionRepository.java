package com.nextflix.app.repositories.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.entities.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    public SubscriptionDto findByUser(UserDto user);
}
