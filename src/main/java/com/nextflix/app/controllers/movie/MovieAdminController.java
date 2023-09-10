package com.nextflix.app.controllers.movie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;

import com.nextflix.app.dtos.movie.MovieAddDto;
import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.enums.ServerType;
import com.nextflix.app.services.interfaces.movie.MovieAdminService;
import com.nextflix.app.services.interfaces.server.ServerService;

import ch.qos.logback.core.encoder.EchoEncoder;
import reactor.core.publisher.Flux;

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
    private WebClient webClient;

    @GetMapping("/getallmovies")
    public ResponseEntity<?> getAllMovies() {

        try {
            List<MovieAdminDto> allMovies = movieAdminService.getAllMovies();
            return ResponseEntity.ok(allMovies);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    private void sendVideoToEncoder(long id, MultipartFile video, ServerDto server)
            throws MalformedURLException, IOException {

        // Thanks Chat GPT

        String url = "http://" + server.getUrl() + ":" + server.getPort() + "/encode?id=" + id;

        String boundary = UUID.randomUUID().toString();

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

 

        writer.append("--" + boundary).append("\r\n");
        writer.append(
                "Content-Disposition: form-data; name=\"file\"; filename=\"" + video.getOriginalFilename() + "\"")
                .append("\r\n");
        writer.append("Content-Type: text/plain").append("\r\n");
        writer.append("\r\n");
        writer.flush();

        InputStream inputStream = video.getInputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        inputStream.close();
        writer.append("\r\n");

        writer.append("--" + boundary + "--").append("\r\n");

        writer.close();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        System.out.println("Response code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
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

            return ResponseEntity.status(400).body(e.getMessage());
        }

        ServerDto server = serverService.getServersByType(ServerType.ENCODING);

        try {

            sendVideoToEncoder(movieAdminDto.getId(), video, server);

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error sending video to encoding server. Error: " + e.getMessage());
        }

        return ResponseEntity.status(200).body("Successful");
    }

    @PutMapping("/updatemovie")
    public ResponseEntity<?> updateMovie(@RequestBody MovieAdminDto movieAdminDto) {
        try {
            MovieAdminDto mov = movieAdminService.updateMovie(movieAdminDto);
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
