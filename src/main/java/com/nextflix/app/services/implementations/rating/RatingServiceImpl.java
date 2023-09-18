package com.nextflix.app.services.implementations.rating;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.rating.RateDto;
import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.entities.Rating;
import com.nextflix.app.repositories.rating.RatingRepository;
import com.nextflix.app.services.interfaces.rating.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating rateMovie(RatingDto ratingDto) {

        return ratingRepository.save(new Rating(ratingDto));
    }

    @Override
    public RatingDto findByMovieIdAndUserId(RateDto rate) {
        try{
        Optional<Rating> rating = ratingRepository.findByUserIdAndMovieId(rate.getMovieId(), rate.getUserID());
        RatingDto ratingDto = null;

        if (rating.isPresent())
            ratingDto = new RatingDto(rating.get());
                    return ratingDto;

        }catch(Exception e){
            System.out.println("Error finding rating. Error: "  + e.getMessage());
            return null;
        }
    }
}
