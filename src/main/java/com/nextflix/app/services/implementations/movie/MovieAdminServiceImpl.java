package com.nextflix.app.services.implementations.movie;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.entities.Movie;
import com.nextflix.app.repositories.movie.MovieAdminRepository;
import com.nextflix.app.services.interfaces.movie.MovieAdminService;

@Service
public class MovieAdminServiceImpl implements MovieAdminService {

    @Autowired
    private MovieAdminRepository movieRepository;

    @Override
    public List<MovieAdminDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        return movies.stream().map((movie) -> new MovieAdminDto(movie)).collect(Collectors.toList());
    }

    @Override
    public MovieAdminDto getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);

        return new MovieAdminDto(movie);
    }

    @Override
    public MovieAdminDto addMovie(MovieAdminDto movieDto) throws Exception {
        Movie movie = movieRepository.findByTitle(movieDto.getTitle());

        if (movie != null) {
            throw new Exception("Movie already exists with this title.");
        }

        Movie newMovie = new Movie(movieDto);
        
        return new MovieAdminDto(movieRepository.save(newMovie));
    }

    @Override
    public MovieAdminDto updateMovie(MovieAdminDto movieDto) {

        Movie movie = new Movie(movieDto);

        return new MovieAdminDto(movieRepository.saveAndFlush(movie));
    }

    @Override
    public void removeMovie(MovieAdminDto movie) {

        movieRepository.deleteById(movie.getId());
    }
}
