package com.nextflix.app.controllers.movie;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.services.interfaces.movie.MovieAdminService;

@RestController
@RequestMapping("/api/v1/admin/movie") 
public class MovieAdminController {
    @Autowired
    private MovieAdminService movieAdminService;

    @GetMapping("/getallmovies")
    public ResponseEntity<?> getAllMovies() {

        try {  
             List<MovieAdminDto> allMovies = movieAdminService.getAllMovies();
            return ResponseEntity.ok(allMovies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/addmovie")
    public ResponseEntity<?> addMovie(@RequestBody MovieAdminDto movieAdminDto) throws Exception {
        
        try {
             movieAdminService.addMovie(movieAdminDto);

            return ResponseEntity.ok().build();

        } catch (Exception e) {

            return ResponseEntity.status(400).build();
        }
    }

    @PutMapping("/updatemovie")
    public ResponseEntity<?> updateMovie(@RequestBody MovieAdminDto movieAdminDto){
        try {
            movieAdminService.updateMovie(movieAdminDto);
            return ResponseEntity.ok().build();
        } catch(Exception e){
            return ResponseEntity.status(400).build();
        }
    }

    @DeleteMapping("/deletemovie")
    public ResponseEntity<?> deleteMovie(@RequestBody MovieAdminDto movieAdminDto){
        try{
            movieAdminService.removeMovie(movieAdminDto);
            return ResponseEntity.ok().build();

        } catch (Exception e){
            return ResponseEntity.status(400).build();
        }
    }
}
