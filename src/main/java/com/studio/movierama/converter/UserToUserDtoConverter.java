package com.studio.movierama.converter;

import com.studio.movierama.domain.User;
import com.studio.movierama.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        UserDto userDto = UserDto
                .builder()
                .id(source.getId())
                .username(source.getUsername())
                .password(source.getPassword())
                .build();
        return userDto;
    }
}
