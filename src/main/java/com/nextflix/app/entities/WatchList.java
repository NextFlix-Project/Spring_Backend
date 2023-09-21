package com.nextflix.app.entities;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "watchlist")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

}
