package com.nextflix.app.services.implementations.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.entities.Rating;
import com.nextflix.app.repositories.rating.RatingRepository;
import com.nextflix.app.services.interfaces.rating.RatingService;

@Service
public class RatingServiceImpl implements RatingService{

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating rateMovie(RatingDto ratingDto) {
   
        return ratingRepository.save(new Rating(ratingDto));
    }
    
}
