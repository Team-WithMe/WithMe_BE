package com.withme.api.controller;


import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //given
    String email = "joinTest@withme.com";
    String username = "위드미 테스트";
    String password = "12345";
    String nickname = "vV위드미짱짱Vv";


    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        userRepository.delete(userRepository.findByEmail(this.email));
    }

    @Test
    public void User_회원가입() throws Exception{
        //given
        JoinRequestDto dto = JoinRequestDto.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .build();

        String url = "http://localhost:" + port + "/join";

        //when
        mvc.perform(post(url)   //생성된 MockMvc를 통해 API를 테스트
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"" + dto.getUsername() + "\"" +
                                ",\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"12345\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

        //then
                .andExpect(status().isOk())
                .andExpect(content().string("join completed"));

        assertThat(userRepository.findByEmail(this.email).getNickname()).isEqualTo(this.nickname);
    }


}
