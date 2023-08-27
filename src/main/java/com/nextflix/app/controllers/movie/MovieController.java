package com.nextflix.app.controllers.movie;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.dtos.rating.RatingRequestDto;
import com.nextflix.app.dtos.user.UserResponseDto;
import com.nextflix.app.services.interfaces.movie.MovieService;
import com.nextflix.app.services.interfaces.rating.RatingService;
import com.nextflix.app.services.interfaces.user.UserService;

@RestController
@RequestMapping("/api/v1/movie")
public class MovieController {
    
    @Autowired
    private MovieService movieService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private UserService userService;

    @GetMapping("/getallmovies")
    public ResponseEntity<?> getAllMovies() {

        try {  
            List<MovieDto> allMovies = movieService.getActiveMovies();
            return ResponseEntity.ok(allMovies);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ratemovie")
    public ResponseEntity<?> rateMovie(@RequestBody RatingRequestDto ratingReq, Principal principal){

        try {
            RatingDto ratingDto = new RatingDto();
            ratingDto.setMovie(movieService.findById(ratingReq.getMovieId()));
            ratingDto.setRating(ratingReq.getRating());
            ratingDto.setUser(new UserResponseDto(userService.getUserByEmail(principal.getName())));

            ratingService.rateMovie(ratingDto);
            return ResponseEntity.ok().build();
        }
        catch(Exception e){
            return ResponseEntity.status(400).body(e.getMessage());

        }
    }
}
