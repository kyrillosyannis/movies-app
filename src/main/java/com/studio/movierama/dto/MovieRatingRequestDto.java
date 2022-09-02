package com.studio.movierama.dto;

import com.studio.movierama.enums.LikeHateFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieRatingRequestDto {

    private Long userId;
    private Long movieId;
    private LikeHateFlag likeHateFlag;
    private boolean retract;
}
