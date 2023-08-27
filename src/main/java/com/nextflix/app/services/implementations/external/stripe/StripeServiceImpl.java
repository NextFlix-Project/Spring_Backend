package com.nextflix.app.services.implementations.external.stripe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.payment.CreditCardDto;
import com.nextflix.app.dtos.payment.CustomerDto;
import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.entities.SubscriptionProduct;
import com.nextflix.app.repositories.porduct.ProductRepository;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;
import com.nextflix.app.services.interfaces.subscription.SubscriptionService;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;

import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod;

@Service
public class StripeServiceImpl implements StripeService {

    @Value("${stripe_apikey}")
    private String apiKey;

    @Autowired
    @Lazy
    SubscriptionService subscriptionService;

    @Autowired
    ProductRepository productRepository;

    @Override
    public SubscriptionProductDto createNewSubscription(Long amount, String name, String description) {
        try {
            Product product = createProduct(name);
            Price price = createPricing(amount, product);
            SubscriptionProductDto newProduct = new SubscriptionProductDto(price.getId(), name, description,
                    price.getUnitAmount());
            productRepository.save(new SubscriptionProduct(newProduct));
            return new SubscriptionProductDto(price.getId(), name, description, price.getUnitAmount());
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @Override

    public Product createProduct(String name) throws StripeException {
        Stripe.apiKey = apiKey;

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);

        return Product.create(params);
    }

    @Override
    public Price createPricing(Long price, Product product) throws StripeException {
        Stripe.apiKey = apiKey;

        Map<String, Object> recurring = new HashMap<>();
        recurring.put("interval", "month");

        Map<String, Object> params = new HashMap<>();
        params.put("unit_amount", price);
        params.put("currency", "usd");
        params.put("recurring", recurring);
        params.put("product", product.getId());

        return Price.create(params);
    }

    @Override
    public Customer createCustomer(CustomerDto customerDto, CreditCardDto creditCardDto) throws StripeException {

        Stripe.apiKey = apiKey;

        PaymentMethod paymentMethod = createPayment(creditCardDto);

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(customerDto.getEmail())
                .setName(customerDto.getName())
                .setPaymentMethod(paymentMethod.getId())
                .setShipping(
                        CustomerCreateParams.Shipping.builder()
                                .setAddress(
                                        CustomerCreateParams.Shipping.Address.builder()
                                                .setCity(customerDto.getCity())
                                                .setCountry(customerDto.getCountry())
                                                .setLine1(customerDto.getAddress())
                                                .setPostalCode(customerDto.getPostalCode())
                                                .setState(customerDto.getState())
                                                .build())
                                .setName(customerDto.getName())
                                .build())
                .setAddress(
                        CustomerCreateParams.Address.builder()
                                .setCity(customerDto.getCity())
                                .setCountry(customerDto.getCountry())
                                .setLine1(customerDto.getAddress())
                                .setPostalCode(customerDto.getPostalCode())
                                .setState(customerDto.getState())
                                .build())
                .build();

        return Customer.create(params);
    }

    @Override
    public PaymentMethod createPayment(CreditCardDto creditCardDto) throws StripeException {

        Stripe.apiKey = apiKey;

        Map<String, Object> card = new HashMap<>();
        card.put("number", creditCardDto.getCreditCardNumber());
        card.put("exp_month", creditCardDto.getExpirationMonth());
        card.put("exp_year", creditCardDto.getExpirationYear());
        card.put("cvc", creditCardDto.getCvc());

        Map<String, Object> params = new HashMap<>();
        params.put("type", "card");
        params.put("card", card);

        return PaymentMethod.create(params);
    }

    @Override
    public Map<String, Object> purchaseSubscription(String customerId, SubscriptionDto subscriptionDto)
            throws StripeException {

        String priceId = subscriptionDto.getStripeId();

        SubscriptionCreateParams.PaymentSettings paymentSettings = SubscriptionCreateParams.PaymentSettings
                .builder()
                .setSaveDefaultPaymentMethod(SaveDefaultPaymentMethod.ON_SUBSCRIPTION)
                .build();

        SubscriptionCreateParams subCreateParams = SubscriptionCreateParams
                .builder()
                .setCustomer(customerId)
                .addItem(
                        SubscriptionCreateParams.Item.builder()
                                .setPrice(priceId)
                                .build())
                .setPaymentSettings(paymentSettings)
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .addAllExpand(Arrays.asList("latest_invoice.payment_intent"))
                .build();

        Subscription subscription = Subscription.create(subCreateParams);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("subscriptionId", subscription.getId());
        responseData.put("clientSecret",
                subscription.getLatestInvoiceObject().getPaymentIntentObject().getClientSecret());
        return responseData;
    }

    @Override
    public void cancelSubscription(CustomerDto customerDto, SubscriptionDto subscriptionDto) throws StripeException {

        try {
            Stripe.apiKey = apiKey;

            Subscription subscription = Subscription.retrieve(subscriptionDto.getStripeId());

            subscription.cancel();
        } catch (StripeException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public SubscriptionProductDto getSubscription() {
        return null;
    }
}
