package com.studio.movierama.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_movies")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMovie {

    @EmbeddedId
    private UserMovieId userMovieId;

    @Column
    private String likeHateFlag;

//    @ManyToOne
//    @MapsId("userId")
//    @EqualsAndHashCode.Include
//    private User user;
//
//    @ManyToOne
//    @MapsId("movieId")
//    @EqualsAndHashCode.Include
//    private Movie movie;
}
