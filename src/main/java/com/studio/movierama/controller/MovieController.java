package com.studio.movierama.controller;

import com.studio.movierama.dto.MovieDto;
import com.studio.movierama.dto.MovieRatingRequestDto;
import com.studio.movierama.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MovieController {

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping(value = "/movies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MovieDto> create(@RequestBody MovieDto movieDto) {
        movieDto = movieService.save(movieDto);
        return new ResponseEntity<>(movieDto, HttpStatus.CREATED);
    }

    @PostMapping(value = "/movies/rate", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity rate(@RequestBody MovieRatingRequestDto movieRatingRequestDto) {
        movieService.rate(movieRatingRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/movies")
    public Page<MovieDto> findAll(@RequestParam("sortBy") String sortBy,
                                  @RequestParam("sortDirection") String sortDirection,
                                  @RequestParam("page") Integer page,
                                  @RequestParam("pageSize") Integer pageSize) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);
        return movieService.findAll(pageable);
    }
}
