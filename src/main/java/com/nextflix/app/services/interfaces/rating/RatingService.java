package com.nextflix.app.services.interfaces.rating;

import com.nextflix.app.dtos.rating.RateDto;
import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.entities.Rating;

public interface RatingService {
    Rating rateMovie(RatingDto ratingDto);
    RatingDto findByMovieIdAndUserId(RateDto rate);
}
