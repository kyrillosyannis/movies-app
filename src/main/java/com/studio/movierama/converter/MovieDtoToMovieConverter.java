package com.studio.movierama.converter;

import com.studio.movierama.domain.Movie;
import com.studio.movierama.dto.MovieDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieDtoToMovieConverter implements Converter<MovieDto, Movie> {

    @Override
    public Movie convert(MovieDto source) {
        Movie movie = Movie
                .builder()
                .title(source.getTitle())
                .description(source.getDescription())
                .userId(source.getUserId())
                .username(source.getUsername())
                .publicationDate(source.getPublicationDate())
                .likes(source.getLikes())
                .hates(source.getHates())
                .build();
        return movie;
    }
}
