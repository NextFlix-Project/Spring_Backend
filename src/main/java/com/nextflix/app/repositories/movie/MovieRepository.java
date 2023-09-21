package com.nextflix.app.repositories.movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m WHERE m.active = true AND m.releaseDate <= CURRENT_DATE")
    List<Movie> findAvailableMovies();
}
