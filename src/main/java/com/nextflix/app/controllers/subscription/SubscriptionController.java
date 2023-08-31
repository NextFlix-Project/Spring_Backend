package com.nextflix.app.controllers.subscription;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.payment.ClientSecretDto;
import com.nextflix.app.dtos.payment.CustomerDto;
import com.nextflix.app.dtos.payment.PaymentIntentDto;
import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.subscription.SubscriptionUserInfoDto;
import com.nextflix.app.dtos.user.UserResponseDto;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;
import com.nextflix.app.services.interfaces.payment.PaymentService;
import com.nextflix.app.services.interfaces.product.ProductService;
import com.nextflix.app.services.interfaces.subscription.SubscriptionService;
import com.nextflix.app.services.interfaces.user.UserService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody CustomerDto customerDto, Principal principal) {
        try {

            SubscriptionDto subscriptionDto = stripeService.purchaseSubscription(customerDto, principal);
            subscriptionDto.setUserId(userService.getUserByEmail(principal.getName()).getId());

            subscriptionService.subscribe(subscriptionDto);

            return ResponseEntity.status(200).body(subscriptionDto);

        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancel(Principal principal) throws StripeException {
        try {
            stripeService.cancelSubscription(principal);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }

    }

    @GetMapping("getsecretkey")
    public ResponseEntity<?> getClientSecret(Principal principal) {

        try {

            String clientSecret = stripeService.getClientSecret();
            return ResponseEntity.status(200).body(new ClientSecretDto(clientSecret));

        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    @PostMapping("/confirmsubscription")
    public ResponseEntity<?> confirm(@RequestBody PaymentIntentDto paymentIntentDto, Principal principal) {
        // Activate subscription here add db record
        // Also add payment record
        try {
            boolean successful = stripeService.confirmPaymentIntent(paymentIntentDto);
            if (successful)
                return ResponseEntity.status(200).build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.status(400).build();

    }

    @GetMapping("/getsubscription")
    public ResponseEntity<?> getSubscription(Principal principal) {
        try {
            SubscriptionProductDto subscriptionProductDto = productService.getProduct();
            UserResponseDto userResponseDto = new UserResponseDto(userService.getUserByEmail(principal.getName()));

            SubscriptionUserInfoDto subscriptionUserInfoDto = new SubscriptionUserInfoDto(subscriptionProductDto,
                    userResponseDto);
            return ResponseEntity.status(200).body(subscriptionUserInfoDto);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/isactive")
    public ResponseEntity<?> getIsSubscriptionActive(Principal principal) {
      try {
            boolean isActive = stripeService.isSubscriptionActive(principal);
            return ResponseEntity.status(200).body(isActive);
        } catch (Exception e) {
            return ResponseEntity.status(400).build();
        }

    }
}