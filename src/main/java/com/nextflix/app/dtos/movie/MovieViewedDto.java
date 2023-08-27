package com.nextflix.app.dtos.movie;

import java.sql.Timestamp;

import com.nextflix.app.entities.MovieViewed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieViewedDto {       
    private Long id;
    private MovieDto movie;
    private Timestamp timestamp;

    public MovieViewedDto(MovieViewed movieViewed){
        this.id = movieViewed.getId();
        this.movie = new MovieDto(movieViewed.getMovie());
        this.timestamp = movieViewed.getTimeStamp();
    }
}
