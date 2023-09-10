package com.nextflix.app.services.interfaces.movie;

import java.util.List;

import com.nextflix.app.dtos.movie.MovieAddDto;
import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.entities.Movie;

public interface MovieAdminService {

        List<MovieAdminDto> getAllMovies();

        MovieAdminDto getMovieDtoById(Long id);

        Movie getMovieById(Long id);

        MovieAdminDto addMovie(MovieAddDto movieDto) throws Exception;

        MovieAdminDto updateMovie(MovieAdminDto movieDto);

        void removeMovie(MovieAdminDto movieDto);
}
