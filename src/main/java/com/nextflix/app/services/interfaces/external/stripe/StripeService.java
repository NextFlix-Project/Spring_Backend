package com.nextflix.app.services.interfaces.external.stripe;

import java.security.Principal;
import java.util.Map;

import com.nextflix.app.dtos.payment.CreditCardDto;
import com.nextflix.app.dtos.payment.CustomerAndPaymentDto;
import com.nextflix.app.dtos.payment.CustomerDto;
import com.nextflix.app.dtos.payment.PaymentIntentDto;
import com.nextflix.app.dtos.payment.PaymentTokenDto;
import com.nextflix.app.dtos.payment.SubscriptionPaymentDto;
import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Price;
import com.stripe.model.Product;

public interface StripeService {
    public SubscriptionProductDto createNewSubscription(Long amount, String name, String description);

    public Product createProduct(String name) throws StripeException;

    public Price createPricing(Long price, Product product) throws StripeException;
    public boolean confirmPaymentIntent(PaymentIntentDto paymentIntent) throws StripeException;

    public CustomerAndPaymentDto createCustomer(CustomerDto customerDto, PaymentTokenDto creditCardDto) throws StripeException;

    public String getClientSecret() throws StripeException;
    public PaymentMethod createPayment(CreditCardDto creditCardDto) throws StripeException;
    public Map<String, Object> purchaseSubscription(SubscriptionPaymentDto subscriptionPaymentDto, Principal principal)
            throws StripeException;

    public void cancelSubscription(CustomerDto customerDto, SubscriptionDto subscriptionDto) throws StripeException;

    public SubscriptionProductDto getSubscription();

}
