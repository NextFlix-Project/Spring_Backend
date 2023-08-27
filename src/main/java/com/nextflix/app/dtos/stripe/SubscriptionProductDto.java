package com.nextflix.app.dtos.stripe;

import com.nextflix.app.entities.SubscriptionProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionProductDto {
    private Long id;
    private String productId;
    private String name;
    private String description;
    private Long price;
    
    public SubscriptionProductDto(SubscriptionProduct product){
        this.id = product.getId();
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
    }

    public SubscriptionProductDto(String productId, String name, String description, Long price){
        this.id = 0L;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
