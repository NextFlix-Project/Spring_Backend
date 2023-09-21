package com.nextflix.app.controllers.movie;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nextflix.app.dtos.movie.MovieAddDto;
import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.enums.ServerType;
import com.nextflix.app.services.interfaces.filesystem.FileService;
import com.nextflix.app.services.interfaces.movie.MovieAdminService;
import com.nextflix.app.services.interfaces.server.ServerService;

@RestController
@RequestMapping("/api/v1/admin/movie")
public class MovieAdminController {

    @Value("${video_server_apikey}")
    private String validApiKey;

    @Autowired
    private MovieAdminService movieAdminService;

    @Autowired
    private ServerService serverService;

    @Autowired
    private FileService fileService;

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
    public ResponseEntity<?> addMovie(@RequestPart("title") String title,
            @RequestPart("description") String description, @RequestPart("active") String active,
            @RequestPart("releaseDate") String releaseDate, @RequestPart("video") MultipartFile video,
            @RequestPart("image") MultipartFile image) throws Exception {

        MovieAddDto movieAddDto = new MovieAddDto();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");

        try {
            Date parsedDate = dateFormat.parse(releaseDate);
            movieAddDto.setReleaseDate(new Timestamp(parsedDate.getTime()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd HH:mm:ss.SSS");
        }

        movieAddDto.setTitle(title);
        movieAddDto.setDescription(description);

        movieAddDto.setActive(Boolean.parseBoolean(active));

        if (video == null)
            return ResponseEntity.status(400).body("No image file attached");
        if (image == null)
            return ResponseEntity.status(400).body("No video file attached");

        MovieAdminDto movieAdminDto = null;
        
        try {
            movieAdminDto = movieAdminService.addMovie(movieAddDto);

        } catch (Exception e) {
            movieAdminService.removeMovie(movieAdminDto);
            return ResponseEntity.status(400).body(e.getMessage());
        }

        try {
            movieAdminDto
                    .setBoxArtUrl("./uploads/" + movieAdminDto.getId().toString() + "/" + image.getOriginalFilename());
            movieAdminService.updateMovie(movieAdminDto);
        } catch (Exception e) {
            movieAdminService.removeMovie(movieAdminDto);
            return ResponseEntity.status(400).body(e.getMessage());
        }

        try {
            fileService.saveFile(image, "./uploads/" + movieAdminDto.getId().toString());
        } catch (Exception e) {
            movieAdminService.removeMovie(movieAdminDto);
            return ResponseEntity.status(400).body(e.getMessage());
        }

        ServerDto server = serverService.getServersByType(ServerType.ENCODING);

        try {
            movieAdminService.sendVideoToEncoder(movieAdminDto.getId(), video, server);
        } catch (Exception e) {
            movieAdminService.removeMovie(movieAdminDto);
            return ResponseEntity.status(400).body("Error sending video to encoding server. Error: " + e.getMessage());
        }

        return ResponseEntity.status(200).body("Successful");
    }

    @PutMapping("/updatemovie")
    public ResponseEntity<?> updateMovie(@RequestBody MovieAdminDto movieAdminDto) {

        try {
            movieAdminService.updateMovie(movieAdminDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/deletemovie")
    public ResponseEntity<?> deleteMovie(@RequestBody MovieAdminDto movieAdminDto) {

        try {
            movieAdminService.removeMovie(movieAdminDto);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
