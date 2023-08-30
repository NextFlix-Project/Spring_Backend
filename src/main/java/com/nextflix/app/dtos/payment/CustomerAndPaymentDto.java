package com.nextflix.app.dtos.payment;

import com.stripe.model.Customer;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAndPaymentDto {
    private Customer customer;
    private PaymentTokenDto paymentMethod;
}
