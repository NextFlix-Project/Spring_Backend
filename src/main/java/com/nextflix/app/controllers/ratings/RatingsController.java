package com.nextflix.app.controllers.ratings;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.rating.RateDto;
import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.dtos.user.UserResponseDto;
import com.nextflix.app.services.interfaces.movie.MovieService;
import com.nextflix.app.services.interfaces.rating.RatingService;
import com.nextflix.app.services.interfaces.user.UserService;

@RestController
@RequestMapping("/api/v1/rating")
public class RatingsController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @PostMapping("/ratemovie")
    public ResponseEntity<?> addServer(@RequestBody RateDto rating, Principal principal) {

        try {
            UserDto user = userService.getUserByEmail(principal.getName());
            rating.setUserID(user.getId());

            RatingDto ratingDto = ratingService.findByMovieIdAndUserId(rating);

            if (ratingDto == null) {
                ratingDto = new RatingDto();
                ratingDto.setMovie(movieService.findById(rating.getMovieId()));
                ratingDto.setUser(new UserResponseDto(user));
                ratingDto.setRating(rating.getRating());
            } else {
                ratingDto.setRating(rating.getRating());
            }

            ratingService.rateMovie(ratingDto);

            return ResponseEntity.status(200).build();

        } catch (Exception e) {

            return ResponseEntity.status(409).body("Error rating movie. Error: " + e.getMessage());

        }

    }

}
