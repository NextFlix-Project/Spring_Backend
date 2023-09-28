package com.nextflix.app.entities;

import java.util.Set;
import java.sql.Timestamp;
import java.util.HashSet;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "subscription")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean active;

    @Column
    private Timestamp startDate;

    @Column
    private Timestamp endDate;

    @Column
    private boolean autoRenew;

    @Column
    private String stripeId;

    @OneToOne()
    @JsonBackReference(value="user-subscription")
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "subscription")
    @JsonManagedReference
    private Set<Payment> payments = new HashSet<>();
}
