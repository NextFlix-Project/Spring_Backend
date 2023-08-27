package com.nextflix.app.services.interfaces.subscription;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;

public interface SubscriptionService {
    SubscriptionDto subscribe(UserDto user);
    boolean isSubscriptionActive(UserDto user);
    SubscriptionDto getSubscriptionByUser(UserDto user);

}
