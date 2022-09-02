package com.studio.movierama.service;

import com.studio.movierama.config.security.MovieRamaUserDetails;
import com.studio.movierama.domain.User;
import com.studio.movierama.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovieRamaUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public MovieRamaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user
                .map(MovieRamaUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Login error"));
    }
}
