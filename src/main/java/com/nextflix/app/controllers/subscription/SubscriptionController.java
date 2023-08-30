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
import com.nextflix.app.dtos.payment.PaymentIntentDto;
import com.nextflix.app.dtos.payment.SubscriptionPaymentDto;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;

@RestController
@RequestMapping("/api/v1/subscription")
public class SubscriptionController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestBody SubscriptionPaymentDto subscriptionDto, Principal principal) {
        try {

            stripeService.purchaseSubscription(subscriptionDto, principal);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancel(Principal principal) {
        return ResponseEntity.status(400).build();

    }

    @GetMapping("getsecretkey")
    public ResponseEntity<?> getClientSecret(Principal principal) {

        try{

            String clientSecret = stripeService.getClientSecret();
            return ResponseEntity.status(200).body(new ClientSecretDto(clientSecret));

        }
        catch(Exception e){
                        return ResponseEntity.status(400).body(e.getMessage());

        }
    }

    @PostMapping("/confirmsubscription")
    public ResponseEntity<?> confirm(@RequestBody PaymentIntentDto paymentIntentDto, Principal principal) {
        //Activate subscription here
        try{
        boolean successful = stripeService.confirmPaymentIntent(paymentIntentDto);
        if (successful)
            return ResponseEntity.status(200).build();
        }
        catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
        
        return ResponseEntity.status(400).build();

    }
}