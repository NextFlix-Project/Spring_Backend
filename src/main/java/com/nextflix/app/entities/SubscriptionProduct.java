package com.nextflix.app.entities;

import com.nextflix.app.dtos.stripe.SubscriptionProductDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subscription_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Long price;

    @Column
    private String productId;

    public SubscriptionProduct(SubscriptionProductDto productDto){
        this.name = productDto.getName();
        this.description = productDto.getDescription();
        this.price = productDto.getPrice();
        this.productId = productDto.getProductId();
    }

}
