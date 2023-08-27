package com.nextflix.app.dtos.payment;

import java.sql.Timestamp;

import com.nextflix.app.dtos.subscription.SubscriptionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private SubscriptionDto subscription;
    private Timestamp date;
}
