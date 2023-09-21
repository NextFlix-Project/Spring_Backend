package com.nextflix.app.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nextflix.app.dtos.movie.MovieAddDto;
import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.dtos.movie.MovieDto;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "movie")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String url;

    @Column
    private String boxArtUrl;

    @Column
    private Boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp releaseDate;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    @JsonManagedReference
    private List<WatchList> watchList = new ArrayList<>();

    public Movie(MovieDto movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.url = movie.getUrl();
    }

    public Movie(MovieAdminDto movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.url = movie.getUrl();
        this.boxArtUrl = movie.getBoxArtUrl();
        this.active = movie.isActive();
        this.releaseDate = movie.getReleaseDate();
    }

    public Movie(MovieAddDto movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.url = movie.getUrl();
        this.boxArtUrl = movie.getBoxArtUrl();
        this.active = movie.isActive();
        this.releaseDate = movie.getReleaseDate();
    }
}
