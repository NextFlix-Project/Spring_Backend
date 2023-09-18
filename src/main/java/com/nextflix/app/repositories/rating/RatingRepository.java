package com.nextflix.app.repositories.rating;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.Rating;
 
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r WHERE r.movie.id = :movieId AND r.user.id = :userId")
    Optional<Rating> findByUserIdAndMovieId(@Param("movieId") Long movieId, @Param("userId") Long userId);
}
