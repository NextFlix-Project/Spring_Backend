package com.nextflix.app.services.implementations.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nextflix.app.dtos.movie.MovieAddDto;
import com.nextflix.app.dtos.movie.MovieAdminDto;
import com.nextflix.app.dtos.server.ServerDto;
import com.nextflix.app.entities.Movie;
import com.nextflix.app.repositories.movie.MovieAdminRepository;
import com.nextflix.app.services.interfaces.movie.MovieAdminService;

@Service
public class MovieAdminServiceImpl implements MovieAdminService {

    @Autowired
    private MovieAdminRepository movieRepository;

    @Override
    public List<MovieAdminDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll(Sort.by(Sort.Order.asc("id")));

        return movies.stream().map((movie) -> new MovieAdminDto(movie)).collect(Collectors.toList());
    }

    @Override
    public MovieAdminDto getMovieDtoById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);

        return new MovieAdminDto(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        Optional<Movie> movie = movieRepository.findById(id);

        if (movie.isPresent())
            return movie.get();

        return null;
    }

    @Override
    public MovieAdminDto addMovie(MovieAddDto movieDto) throws Exception {
        Movie movie = movieRepository.findByTitle(movieDto.getTitle());

        if (movie != null) {
            throw new Exception("Movie already exists with this title.");
        }

        Movie newMovie = new Movie(movieDto);

        return new MovieAdminDto(movieRepository.save(newMovie));
    }

    @Override
    public MovieAdminDto updateMovie(MovieAdminDto movieDto) {

        Movie movie = getMovieById(movieDto.getId());

        movie.setActive(movieDto.isActive());
        movie.setBoxArtUrl(movieDto.getBoxArtUrl());
        movie.setUrl(movieDto.getUrl());
        movie.setBoxArtUrl(movie.getBoxArtUrl());
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setReleaseDate(movieDto.getReleaseDate());
        return new MovieAdminDto(movieRepository.saveAndFlush(movie));
    }

    @Override
    public void removeMovie(MovieAdminDto movie) {

        movieRepository.deleteById(movie.getId());

    }

    @Override
    public void sendVideoToEncoder(long id, MultipartFile video, ServerDto server)
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
}
