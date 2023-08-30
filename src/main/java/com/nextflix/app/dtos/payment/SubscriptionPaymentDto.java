package com.nextflix.app.dtos.payment;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPaymentDto {
    private CustomerDto customer;
    private PaymentTokenDto payment;

}
