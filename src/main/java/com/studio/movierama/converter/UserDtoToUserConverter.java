package com.studio.movierama.converter;

import com.studio.movierama.domain.User;
import com.studio.movierama.dto.UserDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {

    @Override
    public User convert(UserDto userDto) {
        User user = User
                .builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .build();
        return user;
    }
}
