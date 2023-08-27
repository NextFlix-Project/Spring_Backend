package com.nextflix.app.repositories.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextflix.app.entities.MovieViewed;

@Repository
public interface MovieViewedRepository extends JpaRepository<MovieViewed, Long> {

}