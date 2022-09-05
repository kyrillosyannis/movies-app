package com.studio.movierama

import com.studio.movierama.domain.User
import com.studio.movierama.dto.UserDto
import com.studio.movierama.repository.UserRepository
import com.studio.movierama.service.UserService
import org.springframework.core.convert.ConversionService
import spock.lang.Specification

class UserServiceSpec extends Specification {

    UserRepository userRepository = Mock(UserRepository)
    ConversionService conversionService = Mock(ConversionService)
    UserService userService = new UserService(userRepository, conversionService)

    def "save"() {
        given: "a request to save a user"
            UserDto userDto = new UserDto()
        when: "the method is called"
            userService.save(userDto)
        then: "the repository method is called once & conversionService twice"
            1 * userRepository.save(_)
            1 * conversionService.convert(_, User.class)
            1 * conversionService.convert(_, UserDto.class)
    }

    def "find by username"() {
        when: "the method is called"
            userService.findByUsername("username")
        then: "repository and conversionService are each called once"
            1 * userRepository.findByUsername("username") >> Optional.of(new User())
            1 * conversionService.convert(_, UserDto.class)
    }
}
