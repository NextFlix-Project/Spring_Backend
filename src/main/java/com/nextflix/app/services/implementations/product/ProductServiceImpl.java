package com.nextflix.app.services.implementations.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.stripe.SubscriptionProductDto;
import com.nextflix.app.entities.SubscriptionProduct;
import com.nextflix.app.repositories.product.ProductRepository;
import com.nextflix.app.services.interfaces.product.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public SubscriptionProduct createProduct(SubscriptionProductDto productDto) {
        return productRepository.save(new SubscriptionProduct(productDto));
    }

    @Override
    public SubscriptionProductDto getProduct() {
        return new SubscriptionProductDto(productRepository.findFirstByOrderById());
    }

    @Override
    public SubscriptionProductDto updateProduct(SubscriptionProductDto product) {
        return new SubscriptionProductDto(productRepository.saveAndFlush(new SubscriptionProduct(product)));
    }
    
}
