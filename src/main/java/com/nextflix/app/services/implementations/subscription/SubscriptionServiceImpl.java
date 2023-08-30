package com.nextflix.app.services.implementations.subscription;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.entities.Subscription;
import com.nextflix.app.repositories.subscription.SubscriptionRepository;
import com.nextflix.app.services.implementations.external.stripe.StripeServiceImpl;
import com.nextflix.app.services.interfaces.subscription.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Autowired
    StripeServiceImpl stripeServiceImpl;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionDto subscribe(UserDto user) {

        Subscription newSubscription = new Subscription();
        newSubscription.setActive(true);
        newSubscription.setAutoRenew(true);
        newSubscription.setStartDate(new Timestamp(System.currentTimeMillis()));
        long thirtyDays = 30L * 24L * 60L * 60L * 1000L; // 30 days in milliseconds

        newSubscription.setEndDate(new Timestamp(System.currentTimeMillis() + thirtyDays));
        SubscriptionDto subscription = new SubscriptionDto(subscriptionRepository.save(newSubscription));

        return subscription;
    }

    @Override
    public boolean isSubscriptionActive(UserDto user) {
        return subscriptionRepository.findByUser(user).isActive();
    }

    @Override
    public SubscriptionDto getSubscriptionByUser(UserDto user) {
      return subscriptionRepository.findByUser(user);
    }
}
