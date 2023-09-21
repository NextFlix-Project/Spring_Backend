package com.nextflix.app.services.interfaces.rating;

import java.util.List;

import com.nextflix.app.dtos.rating.RateDto;
import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.entities.Rating;

public interface RatingService {
    Rating rateMovie(RatingDto ratingDto);

    List<RatingDto> findAll();

    RatingDto findByMovieIdAndUserId(RateDto rate);
}
