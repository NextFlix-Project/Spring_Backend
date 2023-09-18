package com.nextflix.app.controllers.watchlist;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.dtos.watchlist.WatchListDto;
import com.nextflix.app.services.interfaces.movie.MovieService;
import com.nextflix.app.services.interfaces.user.UserService;
import com.nextflix.app.services.interfaces.watchlist.WatchListService;

@RestController
@RequestMapping("/api/v1/watchlist")
public class WatchListController {

    @Autowired
    UserService userService;

    @Autowired
    WatchListService watchListService;

    @Autowired
    MovieService movieService;

    @GetMapping("/get")
    public ResponseEntity<?> getWatchList(Principal principal) {

        try {
            UserDto user = userService.getUserByEmail(principal.getName());

            return ResponseEntity.ok(user.getWatchList());

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWatchList(@RequestBody MovieDto movieDto, Principal principal) {
        try {
            UserDto user = userService.getUserByEmail(principal.getName());
            MovieDto movie = movieService.findById(movieDto.getId());
            watchListService.addToWatchlist(movie, user);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteFromWatchList(@RequestBody WatchListDto watchListDto) {
        try {
            WatchListDto watchList = watchListService.findById(watchListDto.getId());
            watchListService.removeFromWatchList(watchList);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }
}
