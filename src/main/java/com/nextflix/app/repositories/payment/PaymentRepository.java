package com.nextflix.app.repositories.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
