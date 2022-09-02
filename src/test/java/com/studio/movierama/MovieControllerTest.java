package com.studio.movierama;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.movierama.domain.Movie;
import com.studio.movierama.domain.UserMovie;
import com.studio.movierama.domain.UserMovieId;
import com.studio.movierama.dto.MovieDto;
import com.studio.movierama.dto.MovieRatingRequestDto;
import com.studio.movierama.enums.LikeHateFlag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class MovieControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    public void create() throws Exception {
        MovieDto movie = MovieDto.builder().title("movie title").userId(1L).description("movie desc").build();
        this.mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(movie)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void like() throws Exception {
        MovieRatingRequestDto movieRatingRequestDto = MovieRatingRequestDto
                .builder()
                .movieId(1L)
                .userId(2L)
                .likeHateFlag(LikeHateFlag.LIKE)
                .build();
        this.mockMvc.perform(post("/movies/rate")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(movieRatingRequestDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
