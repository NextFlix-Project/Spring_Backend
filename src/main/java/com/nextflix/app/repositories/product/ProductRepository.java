package com.nextflix.app.repositories.product;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.SubscriptionProduct;

@Repository
public interface ProductRepository extends JpaRepository<SubscriptionProduct, Long> {
    SubscriptionProduct findFirstByOrderById();
}
