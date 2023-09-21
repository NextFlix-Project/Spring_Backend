package com.nextflix.app.services.implementations.external.stripe;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.payment.CreditCardDto;
import com.nextflix.app.dtos.payment.CustomerAndPaymentDto;
import com.nextflix.app.dtos.payment.CustomerDto;
import com.nextflix.app.dtos.payment.PaymentIntentDto;
import com.nextflix.app.dtos.payment.PaymentTokenDto;
import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.entities.SubscriptionProduct;
import com.nextflix.app.repositories.product.ProductRepository;
import com.nextflix.app.services.interfaces.external.stripe.StripeService;
import com.nextflix.app.services.interfaces.subscription.SubscriptionService;
import com.nextflix.app.services.interfaces.user.UserService;

import com.stripe.Stripe;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SubscriptionCreateParams.PaymentSettings.SaveDefaultPaymentMethod;

@Service
public class StripeServiceImpl implements StripeService {

    @Value("${stripe_apikey}")
    private String apiKey;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

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
            System.err.println(e.getMessage());

            return null;
        }
    }

    @Override

    public Product createProduct(String name) throws StripeException {

        try {
            Stripe.apiKey = apiKey;

            Map<String, Object> params = new HashMap<>();
            params.put("name", name);

            return Product.create(params);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Price createPricing(Long price, Product product) throws StripeException {

        try {
            Stripe.apiKey = apiKey;

            Map<String, Object> recurring = new HashMap<>();
            recurring.put("interval", "month");

            Map<String, Object> params = new HashMap<>();
            params.put("unit_amount", price);
            params.put("currency", "usd");
            params.put("recurring", recurring);
            params.put("product", product.getId());

            return Price.create(params);

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean confirmPaymentIntent(PaymentIntentDto paymentIntent) throws StripeException {

        try {
            Stripe.apiKey = apiKey;

            PaymentIntent payment = PaymentIntent.retrieve(paymentIntent.getIntent());

            String status = payment.getStatus();

            if (status.equals("succeeded"))
                return true;

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return false;
        }

        return false;
    }

    @Override
    public CustomerAndPaymentDto createCustomer(CustomerDto customerDto)
            throws StripeException {

        try {
            Stripe.apiKey = apiKey;

            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(customerDto.getEmail())
                    .setName(customerDto.getName())
                    .setPaymentMethod(customerDto.getPaymentId())
                    .build();

            Customer customer = Customer.create(params);
            return new CustomerAndPaymentDto(customer, new PaymentTokenDto(customerDto.getPaymentId()));

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public PaymentMethod createPayment(CreditCardDto creditCardDto) throws StripeException {

        try {
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

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String getClientSecret() throws StripeException {

        Stripe.apiKey = apiKey;
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(999L)
                    .setCurrency("usd")
                    .addPaymentMethodType("card")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return paymentIntent.getClientSecret();

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String getClientSecret(Principal principal) throws StripeException {

        Stripe.apiKey = apiKey;
        try {
            UserDto user = userService.getUserByEmail(principal.getName());
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setCustomer(user.getStripeCustomerId())
                    .setPaymentMethod(user.getStripePaymentMethodId())
                    .setAmount(999L)
                    .setCurrency("usd")
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            return paymentIntent.getClientSecret();

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public SubscriptionDto purchaseSubscription(CustomerDto customerDto, Principal principal)
            throws StripeException {

        UserDto user = userService.getUserByEmail(principal.getName());

        try {
            String priceId = productRepository.findFirstByOrderById().getProductId();

            String customerId = customerDto.getCustomerId();

            if (customerId == null) {
                customerId = createCustomer(customerDto).getCustomer().getId();
            }

            if (customerId == null)
                throw new InvalidRequestException("Customer information is required", "", "error-code", "400", 400,
                        null);

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
                    .setDefaultPaymentMethod(user.getStripePaymentMethodId())
                    .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                    .addAllExpand(Arrays.asList("latest_invoice.payment_intent"))
                    .build();

            Subscription subscription = Subscription.create(subCreateParams);

            SubscriptionDto subscriptionDto = new SubscriptionDto();
            subscriptionDto.setStripeId(subscription.getId());
            subscriptionDto
                    .setClientSecret(subscription.getLatestInvoiceObject().getPaymentIntentObject().getClientSecret());

            return subscriptionDto;

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return null;
        }

    }

    @Override
    public void cancelSubscription(Principal principal) throws StripeException {

        try {
            UserDto userDto = userService.getUserByEmail(principal.getName());
            SubscriptionDto subscriptionDto = subscriptionService.getSubscriptionByUser(userDto);
            Stripe.apiKey = apiKey;

            Subscription subscription = Subscription.retrieve(subscriptionDto.getStripeId());

            subscription.cancel();

        } catch (StripeException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public Subscription getSubscription(Principal principal) throws StripeException {

        try {
            UserDto userDto = userService.getUserByEmail(principal.getName());
            SubscriptionDto subscriptionDto = subscriptionService.getSubscriptionByUser(userDto);
            Stripe.apiKey = apiKey;

            Subscription subscription = Subscription.retrieve(
                    subscriptionDto.getStripeId());
            return subscription;

        } catch (StripeException e) {

            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isSubscriptionActive(Principal principal) throws StripeException {

        try {
            UserDto userDto = userService.getUserByEmail(principal.getName());
            SubscriptionDto subscriptionDto = subscriptionService.getSubscriptionByUser(userDto);
            Stripe.apiKey = apiKey;

            if (subscriptionDto == null)
                return false;
            Subscription subscription = Subscription.retrieve(
                    subscriptionDto.getStripeId());

            return subscription != null ? subscription.getStatus().equals("active") : false;

        } catch (StripeException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
