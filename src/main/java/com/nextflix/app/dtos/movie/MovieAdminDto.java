package com.nextflix.app.dtos.movie;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.entities.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieAdminDto {
    private Long id;
    private String description;
    private String title;
    private String url;
    private String boxArtUrl;
    private boolean active;
    private Timestamp releaseDate;
    private List<RatingDto> ratings;

    public MovieAdminDto(Movie movie) {
        this.id = movie.getId() != null ? movie.getId() : 0;
        this.description = movie.getDescription() != null ? movie.getDescription() : "";
        this.title = movie.getTitle() != null ? movie.getTitle() : "";
        this.url = movie.getUrl() != null ? movie.getUrl() : "";
        this.boxArtUrl = movie.getBoxArtUrl() != null ? movie.getBoxArtUrl() : "";
        this.active = movie.getActive() != null ? movie.getActive() : false;
        this.releaseDate = movie.getReleaseDate() != null ? movie.getReleaseDate() : Timestamp.valueOf("");
        this.ratings = movie.getRatings() != null
                ? movie.getRatings().stream().map((rating) -> new RatingDto(rating)).collect(Collectors.toList())
                : null;
    }

    public MovieAdminDto(Optional<Movie> movie) {

        movie.ifPresent(mov -> {
            this.id = mov.getId() != null ? mov.getId() : 0;
            this.description = mov.getDescription() != null ? mov.getDescription() : "";
            this.title = mov.getTitle() != null ? mov.getTitle() : "";
            this.url = mov.getUrl() != null ? mov.getUrl() : "";
            this.boxArtUrl = mov.getBoxArtUrl() != null ? mov.getBoxArtUrl() : "";
            this.active = mov.getActive() != null ? mov.getActive() : false;
            this.releaseDate = mov.getReleaseDate() != null ? mov.getReleaseDate() : Timestamp.valueOf("");
            this.ratings = mov.getRatings() != null
                    ? mov.getRatings().stream().map((rating) -> new RatingDto(rating)).collect(Collectors.toList())
                    : null;
        });
    }
}
