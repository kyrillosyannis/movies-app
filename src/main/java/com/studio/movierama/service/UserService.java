package com.studio.movierama.service;

import com.studio.movierama.domain.User;
import com.studio.movierama.dto.UserDto;
import com.studio.movierama.exception.MovieRamaException;
import com.studio.movierama.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private UserRepository userRepository;
    private ConversionService conversionService;

    @Autowired
    public UserService(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    public UserDto save(UserDto userDto) {
        log.info("Saving user");
        User user = conversionService.convert(userDto, User.class);
        try {
            userRepository.save(user);
        } catch (Exception unique) {
            log.error(unique.getMessage());
            throw new MovieRamaException("User already exists");
        }
        userDto = conversionService.convert(user, UserDto.class);
        return userDto;
    }
}
