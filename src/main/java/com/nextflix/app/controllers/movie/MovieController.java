package com.nextflix.app.controllers.movie;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.dtos.movie.MovieListDto;
import com.nextflix.app.dtos.rating.RatingDto;
import com.nextflix.app.dtos.rating.RatingRequestDto;
import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.dtos.user.UserResponseDto;
import com.nextflix.app.dtos.watchlist.WatchListDto;
import com.nextflix.app.enums.ServerType;
import com.nextflix.app.services.interfaces.movie.MovieService;
import com.nextflix.app.services.interfaces.rating.RatingService;
import com.nextflix.app.services.interfaces.server.ServerService;
import com.nextflix.app.services.interfaces.subscription.SubscriptionService;
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

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private ServerService serverService;

    @GetMapping("/getallmovies")
    public ResponseEntity<?> getAllMovies(Principal principal) {

        try {
            List<MovieDto> allMovies = movieService.getActiveMovies();
            UserDto user = userService.getUserByEmail(principal.getName());
            MovieListDto movieListDto = new MovieListDto();
            movieListDto.setMovies(allMovies);
            movieListDto.setRatings(ratingService.findAll());
            movieListDto.setWatchList(user.getWatchList().stream().map((watchList) -> new WatchListDto(watchList))
                    .collect(Collectors.toList()));

            return ResponseEntity.ok(movieListDto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/streammovie")
    public ResponseEntity<?> streamMovie(@RequestBody Map<String, Object> body, Principal principal) {

        try {
            UserDto user = userService.getUserByEmail(principal.getName());
            boolean subscribed = subscriptionService.isSubscriptionActive(user);

            if (!subscribed)
                return ResponseEntity.status(400).body("Not subscribed");

            ServerDto server = serverService.getServersByType(ServerType.STREAMING);

            String url = "http://" + server.getUrl() + ":" + server.getPort() + "/stream?id=" + body.get("id");

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ratemovie")
    public ResponseEntity<?> rateMovie(@RequestBody RatingRequestDto ratingReq, Principal principal) {

        try {
            RatingDto ratingDto = new RatingDto();
            ratingDto.setMovie(movieService.findById(ratingReq.getMovieId()));
            ratingDto.setRating(ratingReq.getRating());
            ratingDto.setUser(new UserResponseDto(userService.getUserByEmail(principal.getName())));

            ratingService.rateMovie(ratingDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());

        }
    }
}
