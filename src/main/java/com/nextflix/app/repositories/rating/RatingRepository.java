package com.nextflix.app.repositories.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
