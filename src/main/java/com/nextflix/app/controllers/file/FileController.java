package com.nextflix.app.controllers.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.movie.MovieDto;
import com.nextflix.app.services.interfaces.filesystem.FileService;
import com.nextflix.app.services.interfaces.movie.MovieService;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    MovieService movieService;

    @GetMapping("/image")
    public ResponseEntity<?> getImage(@RequestParam Long movieId) {

        try {
            MovieDto movieDto = movieService.findById(movieId);
            byte[] file = fileService.getFile(movieDto.getBoxArtUrl());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            return new ResponseEntity<>(file, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
