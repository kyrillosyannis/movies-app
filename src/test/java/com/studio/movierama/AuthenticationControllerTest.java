package com.studio.movierama;

import com.studio.movierama.dto.AuthenticationRequestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.studio.movierama.MovieControllerTest.asJsonString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void authenticate() throws Exception {
        AuthenticationRequestDto authenticationRequestDto = AuthenticationRequestDto
                .builder()
                .username("user1")
                .password("pass1")
                .build();
        this.mockMvc.perform(
                        post("/authenticate").content(asJsonString(authenticationRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
