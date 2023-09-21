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

        try{
        return productRepository.save(new SubscriptionProduct(productDto));
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public SubscriptionProductDto getProduct() {

        try{
        return new SubscriptionProductDto(productRepository.findFirstByOrderById());
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public SubscriptionProductDto updateProduct(SubscriptionProductDto product) {

        try{
        return new SubscriptionProductDto(productRepository.saveAndFlush(new SubscriptionProduct(product)));
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    
}
