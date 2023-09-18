package com.nextflix.app.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nextflix.app.dtos.rating.RatingDto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "rating")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int rating;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Rating(RatingDto ratingDto){
        this.id = ratingDto.getId();
        this.rating = ratingDto.getRating();
        this.movie = new Movie(ratingDto.getMovie());
        this.user = new User(ratingDto.getUser());
    }

}
