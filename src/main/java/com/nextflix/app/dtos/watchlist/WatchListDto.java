package com.nextflix.app.dtos.watchlist;

import java.sql.Timestamp;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.entities.WatchList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListDto {
    private Long id;
    private MovieDto movie;
    private Timestamp createdAt;

    public WatchListDto(WatchList watchList) {
        this.id = watchList.getId();
        this.movie = new MovieDto(watchList.getMovie());
        this.createdAt = watchList.getCreatedAt();
    }
}
