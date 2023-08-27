package com.nextflix.app.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "payment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private Timestamp date;

    @Column
    private boolean successful;

    @ManyToOne
    @JsonBackReference
    private Subscription subscription;

}
