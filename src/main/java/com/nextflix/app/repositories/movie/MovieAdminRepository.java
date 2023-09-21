package com.nextflix.app.repositories.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.Movie;

@Repository
public interface MovieAdminRepository extends JpaRepository<Movie, Long> {
    Movie findByTitle(String name);

}
