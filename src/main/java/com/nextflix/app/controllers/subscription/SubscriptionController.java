package com.nextflix.app.controllers.subscription;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;
import com.nextflix.app.services.interfaces.user.UserService;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private UserService userService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionDto subscriptionDto, Principal principal) {
        UserDto user = userService.getUserByEmail(principal.getName());
        try {
        stripeService.purchaseSubscription(user.getStripeCustomerId(), subscriptionDto);
        }
        catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancel(Principal principal) {
        return ResponseEntity.status(400).build();

    }
}