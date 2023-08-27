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

    public MovieAdminDto(Movie movie){
        this.id = movie.getId();
        this.description = movie.getDescription();
        this.title = movie.getTitle();
        this.url = movie.getUrl();
        this.boxArtUrl = movie.getBoxArtUrl();
        this.active = movie.getActive();
        this.releaseDate = movie.getReleaseDate();
        this.ratings = movie.getRatings().stream().map((rating) -> new RatingDto(rating)).collect(Collectors.toList());
    }

    public MovieAdminDto(Optional<Movie> movie) {

        movie.ifPresent(mov -> {
            this.id = mov.getId() != null ? mov.getId() : null;
            this.description = mov.getDescription() != null ? mov.getDescription() : null;
            this.title = mov.getTitle() != null ? mov.getTitle() : null;
            this.url = mov.getUrl() != null ? mov.getUrl() : null;  
            this.boxArtUrl = mov.getBoxArtUrl() != null ? mov.getBoxArtUrl() : null;
            this.active = mov.getActive() != null ? mov.getActive() : null;
            this.releaseDate = mov.getReleaseDate() != null ? mov.getReleaseDate() : null;
            this.ratings = mov.getRatings() != null ? mov.getRatings().stream().map((rating) -> new RatingDto(rating)).collect(Collectors.toList()) : null;
        });
    }
}
