package com.nextflix.app.services.interfaces.movie;

import java.util.List;

import com.nextflix.app.dtos.movie.MovieDto;

public interface MovieService {
    public List<MovieDto> getActiveMovies();
    public MovieDto findById(Long id);
}
