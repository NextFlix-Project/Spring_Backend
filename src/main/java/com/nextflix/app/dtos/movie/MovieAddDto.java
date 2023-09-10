package com.nextflix.app.dtos.movie;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.nextflix.app.entities.Movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieAddDto {
    private Long id;
    private String description;
    private String title;
    private String url;
    private String boxArtUrl;
    private boolean active;
    private Timestamp releaseDate;
    private MultipartFile video;
    private MultipartFile image;

    public MovieAddDto(Movie movie) {
        this.id = movie.getId();
        this.description = movie.getDescription();
        this.title = movie.getTitle();
        this.url = movie.getUrl();
        this.boxArtUrl = movie.getBoxArtUrl();
        this.active = movie.getActive();
        this.releaseDate = movie.getReleaseDate();
    }

    public MovieAddDto(Optional<Movie> movie) {

        movie.ifPresent(mov -> {
            this.id = mov.getId() != null ? mov.getId() : null;
            this.description = mov.getDescription() != null ? mov.getDescription() : null;
            this.title = mov.getTitle() != null ? mov.getTitle() : null;
            this.url = mov.getUrl() != null ? mov.getUrl() : null;
            this.boxArtUrl = mov.getBoxArtUrl() != null ? mov.getBoxArtUrl() : null;
            this.active = mov.getActive() != null ? mov.getActive() : null;
            this.releaseDate = mov.getReleaseDate() != null ? mov.getReleaseDate() : null;
        });
    }
}
