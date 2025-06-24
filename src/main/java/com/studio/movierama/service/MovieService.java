package com.studio.movierama.service;

import com.studio.movierama.config.security.MovieRamaUserDetails;
import com.studio.movierama.domain.Movie;
import com.studio.movierama.domain.UserMovie;
import com.studio.movierama.domain.UserMovieId;
import com.studio.movierama.dto.MovieDto;
import com.studio.movierama.dto.MovieRatingRequestDto;
import com.studio.movierama.enums.LikeHateFlag;
import com.studio.movierama.exception.MovieRamaException;
import com.studio.movierama.repository.MovieRepository;
import com.studio.movierama.repository.UserMovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MovieService {

    private MovieRepository movieRepository;
    private UserMovieRepository userMovieRepository;
    private ConversionService conversionService;

    @Autowired
    public MovieService(MovieRepository movieRepository, ConversionService conversionService,
                        UserMovieRepository userMovieRepository) {
        this.movieRepository = movieRepository;
        this.conversionService = conversionService;
        this.userMovieRepository = userMovieRepository;
    }

    public MovieDto save(MovieDto movieDto) {
        log.info("saving movie");
        Movie movie = conversionService.convert(movieDto, Movie.class);
        Instant now = Instant.now();
        movie.setPublicationDate(now);
        movieRepository.save(movie);
        movieDto = conversionService.convert(movie, MovieDto.class);
        return movieDto;
    }

    public Page<MovieDto> findAll(Pageable pageable) {
        log.info("finding all movies");
        String username = null;
        Long userId = null;
        MovieRamaUserDetails loggedInUser = getLoggedInUserDetails();
        if (loggedInUser != null) {
            username = loggedInUser.getUsername();
            userId = loggedInUser.getId();
        }
        Page<Movie> movies = movieRepository.findAll(pageable);
        List<MovieDto> movieDtoList = movies
                .stream()
                .map(movie -> conversionService.convert(movie, MovieDto.class))
                .collect(Collectors.toList());
        if (username != null) {
            movieDtoList = setLoggedInUserRatings(userId, movieDtoList);
        }
        Page<MovieDto> movieDtoPage = new PageImpl<>(movieDtoList, pageable, movies.getTotalElements());
        return movieDtoPage;
    }

    private MovieRamaUserDetails getLoggedInUserDetails() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (principal instanceof MovieRamaUserDetails) {
            return (MovieRamaUserDetails) principal;
        }
        return null;
    }

    private List<MovieDto> setLoggedInUserRatings(Long loggedInUserId, List<MovieDto> movies) {
        List<UserMovieId> userMovieIds = movies
                .stream()
                .map(movieDto -> new UserMovieId(loggedInUserId, movieDto.getId()))
                .toList();
        List<UserMovie> userMovies = userMovieRepository.findAllById(userMovieIds);
        return movies.stream()
                .map(movieDto -> {
                    UserMovie userMovie = userMovies.stream()
                            .filter(userMovie1 -> userMovie1.getUserMovieId().getMovieId().equals(movieDto.getId()))
                            .findFirst()
                            .orElse(null);
                    if (userMovie != null) {
                        if (LikeHateFlag.LIKE.getDbValue().equals(userMovie.getLikeHateFlag())) {
                            movieDto.setLikedByUser(true);
                        } else if (LikeHateFlag.HATE.getDbValue().equals(userMovie.getLikeHateFlag())) {
                            movieDto.setHatedByUser(false);
                        }
                    }
                    return movieDto;
                })
                .toList();
    }

    public void rate(MovieRatingRequestDto movieRatingRequestDto) {
        if (movieRatingRequestDto.isRetract()) {
            retractRating(movieRatingRequestDto.getMovieId(), movieRatingRequestDto.getUserId());
            return;
        }
        switch (movieRatingRequestDto.getLikeHateFlag()) {
            case LIKE:
                like(movieRatingRequestDto.getMovieId(), movieRatingRequestDto.getUserId());
                break;
            case HATE:
                hate(movieRatingRequestDto.getMovieId(), movieRatingRequestDto.getUserId());
                break;
        }
    }

    private void hate(Long movieId, Long userId) {
        log.info("Mark movie with id: {} hated by user with id: {}", movieId, userId);
        setLikeHateFlag(movieId, userId, LikeHateFlag.HATE);
    }

    private void like(Long movieId, Long userId) {
        log.info("Mark movie with id: {} liked by user with id: {}", movieId, userId);
        setLikeHateFlag(movieId, userId, LikeHateFlag.LIKE);
    }

    private void retractRating(Long movieId, Long userId) {
        log.info("retracting rating of user: {} for movie: {}", userId, movieId);
        userMovieRepository.deleteById(new UserMovieId(userId, movieId));
    }

    private void setLikeHateFlag(Long movieId, Long userId, LikeHateFlag likeHateFlag) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieRamaException("Movie not found"));
        if (movie.getUserId().equals(userId)) {
            throw new MovieRamaException("User cannot like their own movie");
        }
        UserMovie userMovie = userMovieRepository.findById(new UserMovieId(userId, movieId))
                .orElse(UserMovie
                        .builder()
                        .userMovieId(new UserMovieId(userId, movieId))
                        .build());
        if (likeHateFlag.getDbValue().equals(userMovie.getLikeHateFlag())) {
            return;
        }
        userMovie.setLikeHateFlag(likeHateFlag.getDbValue());
        userMovieRepository.save(userMovie);
    }
}
