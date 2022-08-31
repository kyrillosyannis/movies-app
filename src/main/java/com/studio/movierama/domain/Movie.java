package com.studio.movierama.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class Movie {

    @Id
    @Column
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Long userId;

    @Column
    private Instant publicationDate;

    @Column
    private Integer likes;

    @Column
    private Integer hates;
}
