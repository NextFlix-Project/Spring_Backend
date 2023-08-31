package com.nextflix.app.services.implementations.subscription;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.entities.Subscription;
import com.nextflix.app.entities.User;
import com.nextflix.app.repositories.subscription.SubscriptionRepository;
import com.nextflix.app.repositories.user.UserRepository;
import com.nextflix.app.services.interfaces.subscription.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
 
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SubscriptionDto subscribe(SubscriptionDto subscription) {

        long thirtyDays = 30L * 24L * 60L * 60L * 1000L; // 30 days in milliseconds

        Optional<User> user = userRepository.findById(subscription.getUserId());

        Subscription newSubscription = new Subscription();
        user.ifPresent(usr -> newSubscription.setUser(usr));
        newSubscription.setStripeId(subscription.getStripeId());
        newSubscription.setActive(true);
        newSubscription.setAutoRenew(true);
        newSubscription.setStartDate(new Timestamp(System.currentTimeMillis()));
        newSubscription.setEndDate(new Timestamp(System.currentTimeMillis() + thirtyDays));

        Subscription oldSub = subscriptionRepository.getByUserId(newSubscription.getUser().getId());

        if (oldSub != null) {
            oldSub.setStripeId(subscription.getStripeId());
            oldSub.setActive(true);
            oldSub.setAutoRenew(true);
            oldSub.setStartDate(new Timestamp(System.currentTimeMillis()));
            oldSub.setEndDate(new Timestamp(System.currentTimeMillis() + thirtyDays));
            return new SubscriptionDto(subscriptionRepository.save(oldSub));
        }

        return new SubscriptionDto(subscriptionRepository.save(newSubscription));
    }

    @Override
    public boolean isSubscriptionActive(UserDto user) {
        return new SubscriptionDto(subscriptionRepository.findByUser(new User(user)).get(0)).isActive();
    }

    @Override
    public SubscriptionDto getSubscriptionByUser(UserDto user) {
        return new SubscriptionDto(subscriptionRepository.findByUser(new User(user)).get(0));
    }
}
