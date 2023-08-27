package com.nextflix.app.services.interfaces.rating;

import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.entities.Rating;

public interface RatingService {
    Rating rateMovie(RatingDto ratingDto);
}
