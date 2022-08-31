package com.studio.movierama.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserDto {

    private Long id;
    private String username;
    private String password;
}
