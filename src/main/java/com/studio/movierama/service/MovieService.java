package com.studio.movierama.service;

import com.studio.movierama.domain.Movie;
import com.studio.movierama.dto.MovieDto;
import com.studio.movierama.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MovieService {

    private MovieRepository movieRepository;
    private ConversionService conversionService;

    @Autowired
    public MovieService(MovieRepository movieRepository, ConversionService conversionService) {
        this.movieRepository = movieRepository;
        this.conversionService = conversionService;
    }

    public MovieDto save(MovieDto movieDto) {
        log.info("saving movie");
        Movie movie = conversionService.convert(movieDto, Movie.class);
        movieRepository.save(movie);
        movieDto = conversionService.convert(movie, MovieDto.class);
        return movieDto;
    }
}
