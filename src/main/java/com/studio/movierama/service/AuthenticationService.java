package com.studio.movierama.service;

import com.studio.movierama.config.security.JwtProvider;
import com.studio.movierama.dto.AuthenticationRequestDto;
import com.studio.movierama.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Value("${cookieMaxAgeInMs}")
    private int cookieMaxAge;
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private UserService userService;

    @Autowired
    public AuthenticationService(UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                                 JwtProvider jwtProvider, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    public ResponseEntity<UserDto> authenticate(AuthenticationRequestDto requestDto) throws UsernameNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(requestDto.getUsername());
        String jwt = jwtProvider.generateToken(userDetails);
        UserDto userDto = userService.findByUsername(requestDto.getUsername());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "Bearer=" + jwt + "; " +
                "HttpOnly; Path=/; Max-Age=" + cookieMaxAge + ";");
        ResponseEntity<UserDto> responseEntity = new ResponseEntity(userDto, headers, HttpStatus.OK);
        return responseEntity;
    }
}
