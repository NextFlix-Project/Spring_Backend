package com.nextflix.app.services.interfaces.movie;

import java.util.List;

import com.nextflix.app.dtos.movie.MovieAdminDto;

public interface MovieAdminService {
        
        List<MovieAdminDto> getAllMovies();
        MovieAdminDto getMovieById(Long id);

        MovieAdminDto addMovie(MovieAdminDto movieDto) throws Exception;
        MovieAdminDto updateMovie(MovieAdminDto movieDto);
        void removeMovie(MovieAdminDto movieDto);
}
