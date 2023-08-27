package com.nextflix.app.services.interfaces.product;

import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.entities.SubscriptionProduct;

public interface ProductService {
    SubscriptionProduct createProduct(SubscriptionProductDto productDto);
    SubscriptionProductDto getProduct();
    SubscriptionProductDto updateProduct(SubscriptionProductDto product);
}
