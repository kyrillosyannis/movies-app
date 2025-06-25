package com.studio.movierama.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;
    private UserDetailsService userDetailsService;
    private final RequestMatcher ignoredPath1 = PathPatternRequestMatcher.withDefaults().matcher("/authenticate");
    private final RequestMatcher ignoredPath2 = PathPatternRequestMatcher.withDefaults().matcher("/register");

    @Autowired
    public JwtRequestFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (this.ignoredPath1.matches(request) || this.ignoredPath2.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String bearerToken = null;
        if (request.getHeader("Authorization") != null) {
            bearerToken = request.getHeader("Authorization");

        }
        String username = null;
        String jwt = null;
        if (bearerToken != null && bearerToken.startsWith("Bearer: ")) {
            jwt = bearerToken.substring(8);
            try {
                username = jwtProvider.getUsernameFromJWT(jwt);
            } catch (IllegalArgumentException e) {
                log.info("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.info("JWT Token has expired");
            }
        } else {
            log.info("JWT Token does not begin with Bearer String");
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtProvider.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
