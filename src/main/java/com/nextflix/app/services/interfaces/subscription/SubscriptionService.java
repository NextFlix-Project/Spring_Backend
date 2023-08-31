package com.nextflix.app.services.interfaces.subscription;

import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;

@Service
public interface SubscriptionService {
    SubscriptionDto subscribe(SubscriptionDto subscriptionDto);
    boolean isSubscriptionActive(UserDto user);
    SubscriptionDto getSubscriptionByUser(UserDto user);

}
