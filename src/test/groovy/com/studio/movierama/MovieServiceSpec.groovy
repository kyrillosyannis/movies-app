package com.studio.movierama

import com.studio.movierama.domain.Movie
import com.studio.movierama.domain.UserMovie
import com.studio.movierama.domain.UserMovieId
import com.studio.movierama.dto.MovieDto
import com.studio.movierama.dto.MovieRatingRequestDto
import com.studio.movierama.enums.LikeHateFlag
import com.studio.movierama.repository.MovieRepository
import com.studio.movierama.repository.UserMovieRepository
import com.studio.movierama.service.MovieService
import org.springframework.core.convert.ConversionService
import spock.lang.Specification

class MovieServiceSpec extends Specification {

    MovieRepository movieRepository = Mock(MovieRepository)
    UserMovieRepository userMovieRepository = Mock(UserMovieRepository)
    ConversionService conversionService = Mock(ConversionService)
    MovieService movieService = new MovieService(movieRepository, conversionService, userMovieRepository)

    def "save"() {
        given: "a request to save a movie"
            MovieDto movieDto = new MovieDto()
        when: "the method is called"
            movieService.save(movieDto)
        then: "the repository method is called once & conversionService twice"
            1 * conversionService.convert(_, Movie.class) >> new Movie()
            1 * movieRepository.save(_)
            1 * conversionService.convert(_, MovieDto.class)
    }

    def "retract rating"() {
        given: "a request to retract a movie rating"
            MovieRatingRequestDto movieRatingRequestDto = MovieRatingRequestDto.builder().retract(true).build()
        when: "the method is called"
            movieService.rate(movieRatingRequestDto)
        then: "delete method of userMovieRepository is called"
            1 * userMovieRepository.deleteById(_)
    }

    def "like a movie"() {
        given: "a request to like a movie"
            MovieRatingRequestDto movieRatingRequestDto = MovieRatingRequestDto
                    .builder()
                    .likeHateFlag(LikeHateFlag.LIKE)
                    .userId(1L)
                    .movieId(1L)
                    .build()
            UserMovieId userMovieId = new UserMovieId(1L, 1L)
            UserMovie userMovie = UserMovie
                    .builder()
                    .userMovieId(userMovieId)
                    .likeHateFlag(LikeHateFlag.LIKE.dbValue)
                    .build()
        when: "the method is called"
            movieService.rate(movieRatingRequestDto)
        then: "an object with LIKE flag will be saved to the database"
            1 * movieRepository.findById(_) >> Optional.of(Movie.builder().userId(2L).id(1L).build())
            1 * userMovieRepository.findById(_) >> Optional.of(UserMovie.builder().userMovieId(userMovieId).build())
            1 * userMovieRepository.save(userMovie)
    }

    def "hate a movie"() {
        given: "a request to hate a movie"
            MovieRatingRequestDto movieRatingRequestDto = MovieRatingRequestDto
                    .builder()
                    .likeHateFlag(LikeHateFlag.HATE)
                    .userId(1L)
                    .movieId(1L)
                    .build()
            UserMovieId userMovieId = new UserMovieId(1L, 1L)
            UserMovie userMovie = UserMovie
                    .builder()
                    .userMovieId(userMovieId)
                    .likeHateFlag(LikeHateFlag.HATE.dbValue)
                    .build()
        when: "the method is called"
            movieService.rate(movieRatingRequestDto)
        then: "an object with HATE flag will be saved to the database"
            1 * movieRepository.findById(_) >> Optional.of(Movie.builder().userId(2L).id(1L).build())
            1 * userMovieRepository.findById(_) >> Optional.of(UserMovie.builder().userMovieId(userMovieId).build())
            1 * userMovieRepository.save(userMovie)
    }

    //TODO add rest of the tests
}
