package com.nextflix.app.dtos.movie;

import java.util.Optional;

import com.nextflix.app.entities.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Long id;
    private String description;
    private String title;
    private String url;
    private String boxArtUrl;

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.description = movie.getDescription();
        this.title = movie.getTitle();
        this.url = movie.getUrl();
        this.boxArtUrl = movie.getBoxArtUrl();
    }


    public MovieDto(Optional<Movie> movie) {

        movie.ifPresent(mov -> {
            this.id = mov.getId() != null ? mov.getId() : null;
            this.description = mov.getDescription() != null ? mov.getDescription() : null;
            this.title = mov.getTitle() != null ? mov.getTitle() : null;
            this.url = mov.getUrl() != null ? mov.getUrl() : null;
            this.boxArtUrl = mov.getBoxArtUrl() != null ? mov.getBoxArtUrl() : null;
        });
    }
}
