package com.nextflix.app.dtos.subscription;

import java.sql.Timestamp;

import com.nextflix.app.entities.Subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDto {
    private Long id;
    private boolean active;
    private boolean autoRenew;
    private Timestamp startDate;
    private Timestamp endDate;
    private String stripeId;
    private Long userId;
    private String clientSecret;

    public SubscriptionDto(Subscription subscription) {
        this.id = subscription.getId();
        this.active = subscription.isActive();
        this.autoRenew = subscription.isAutoRenew();
        this.startDate = subscription.getStartDate();
        this.endDate = subscription.getEndDate();
        this.stripeId = subscription.getStripeId();
        this.userId = subscription.getUser().getId();
    }
}
