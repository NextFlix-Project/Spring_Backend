package com.nextflix.app.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String customerId;
    private String paymentId;
    private String email;
    private String name;
    private String city;
    private String country;
    private String address;
    private String state;
    private String postalCode;
    private String paymentMethod;
}
