package com.studio.movierama.enums;

import lombok.Getter;

@Getter
public enum LikeHateFlag {
    LIKE("L"),
    HATE("H");

    private final String dbValue;

    LikeHateFlag(String dbValue) {
        this.dbValue = dbValue;
    }
}
