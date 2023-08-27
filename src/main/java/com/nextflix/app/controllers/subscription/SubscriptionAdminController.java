package com.nextflix.app.controllers.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;

@RestController
@RequestMapping("/api/v1/admin/subscription")
public class SubscriptionAdminController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SubscriptionProductDto productDto) {

        try{
            stripeService.createNewSubscription(productDto.getPrice(), productDto.getName(), productDto.getDescription());
        }
        catch (Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.status(200).build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SubscriptionDto subscriptionDto) {

  
        return ResponseEntity.status(400).build();

    }
}