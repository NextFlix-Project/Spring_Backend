package com.nextflix.app.services.implementations.movie;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.entities.Movie;
import com.nextflix.app.repositories.movie.MovieRepository;
import com.nextflix.app.services.interfaces.movie.MovieService;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<MovieDto> getActiveMovies() {

        try {
            List<Movie> movies = movieRepository.findAvailableMovies();

            return movies.stream().map((movie) -> new MovieDto(movie)).collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public MovieDto findById(Long id) {

        try {
            return new MovieDto(movieRepository.findById(id));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
