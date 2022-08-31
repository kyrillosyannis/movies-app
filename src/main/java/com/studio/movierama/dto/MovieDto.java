package com.studio.movierama.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Builder
@Getter
@Setter
public class MovieDto {

    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Instant publicationDate;
    private Integer likes;
    private Integer hates;
}
