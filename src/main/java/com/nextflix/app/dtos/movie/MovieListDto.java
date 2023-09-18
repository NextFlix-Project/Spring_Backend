package com.nextflix.app.dtos.movie;

import java.util.List;

import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.dtos.watchlist.WatchListDto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MovieListDto {
    private List<MovieDto> movies;
    private List<WatchListDto> watchList;
    private List<RatingDto> ratings;
}
