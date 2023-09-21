package com.nextflix.app.repositories.subscription;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nextflix.app.dtos.subscription.SubscriptionDto;
import com.nextflix.app.entities.Subscription;
import com.nextflix.app.entities.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    public List<Subscription> findByUser(User user);

    @Query(value = "SELECT s FROM Subscription s WHERE s.user_id = :id", nativeQuery = true)
    public SubscriptionDto findByUserId(Long id);

    public Subscription getByUserId(Long id);
}
