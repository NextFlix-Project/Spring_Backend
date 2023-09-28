package com.nextflix.app.controllers.customer;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.payment.CreditCardDto;
import com.nextflix.app.dtos.payment.CustomerAndPaymentDto;
import com.nextflix.app.dtos.payment.CustomerDto;
import com.nextflix.app.dtos.user.UserAdminDashDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;
import com.nextflix.app.services.interfaces.user.UserService;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private StripeService stripeService;
    @Autowired
    private UserService userService;

    @PostMapping("/createpayment")
    public ResponseEntity<?> createPayment(@RequestBody CreditCardDto paymentDto, Principal principal)
            throws StripeException {

        try {

            PaymentMethod paymentMethod = stripeService.createPayment(paymentDto);
            UserDto userDto = userService.getUserByEmail(principal.getName());
            userDto.setStripePaymentMethodId(paymentMethod.getId());
            userService.updateUser(new UserAdminDashDto(userDto));

            return ResponseEntity.ok().build();

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/createcustomer")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customerDto, Principal principal)
            throws StripeException {

        try {

            UserDto userDto = userService.getUserByEmail(principal.getName());
            userDto.setStripePaymentMethodId(customerDto.getPaymentId());

            if (userDto.getStripePaymentMethodId() == null)
                return ResponseEntity.status(400).body("No payment method");

            customerDto.setEmail(principal.getName());

            CustomerAndPaymentDto customerAndPaymentDto = stripeService.createCustomer(customerDto);

            userDto.setStripeCustomerId(customerAndPaymentDto.getCustomer().getId());
            userService.updateUser(new UserAdminDashDto(userDto));

            return ResponseEntity.ok().body(userDto);

        } catch (AuthenticationException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
