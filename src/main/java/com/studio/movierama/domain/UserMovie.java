package com.studio.movierama.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserMovie {

    @EmbeddedId
    private UserMovieId userMovieId;

    @Column
    private String likeHateFlag;

    @ManyToOne
    @MapsId("userId")
    @EqualsAndHashCode.Include
    private User user;

    @ManyToOne
    @MapsId("movieId")
    @EqualsAndHashCode.Include
    private Movie movie;
}
