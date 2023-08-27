package com.nextflix.app.dtos.rating;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.dtos.user.UserResponseDto;
import com.nextflix.app.entities.Rating;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Long id;
    private MovieDto movie;
    private UserResponseDto user;
    private int rating;

    public RatingDto(Rating rating){
        this.id = rating.getId();
        this.movie = new MovieDto(rating.getMovie());
        this.rating = rating.getRating();
        this.user = new UserResponseDto(rating.getUser());
    }
}
