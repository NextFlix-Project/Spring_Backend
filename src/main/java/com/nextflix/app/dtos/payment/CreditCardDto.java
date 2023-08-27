package com.nextflix.app.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDto {
    private String creditCardNumber;
    private String expirationMonth;
    private String expirationYear;
    private String cvc;
}
