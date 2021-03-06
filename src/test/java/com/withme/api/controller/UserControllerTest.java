package com.withme.api.controller;


import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.domain.user.User;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final String dupEmail = "dup@check.com";
    private final String dupNick = "dupNick";

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        JoinRequestDto dto = JoinRequestDto.builder()
                .email(this.dupEmail)
                .password("1234qwer%T")
                .nickname(this.dupNick)
                .build();

        userController.join(dto);
    }

    @AfterEach
    public void tearDown() {
        userRepository.findAll().forEach(user -> userRepository.delete(user));
    }

    @Test
    public void ????????????_??????() throws Exception{
        //given
        String email = "joinTest@withme.com";
        String password = "1234qwer%T";
        String nickname = "vV?????????Vv";

        String apiUrl = "/api/v1/join";

        JoinRequestDto dto = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)   //????????? MockMvc??? ?????? API??? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"1234qwer%T\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

        //then
                .andExpect(status().isCreated());

        assertThat(userRepository.findByEmail(email)
                .map(User::getNickname)).isEqualTo(Optional.of(nickname));
    }

    @Test
    public void ????????????_??????_?????????_?????????() throws Exception{
        //given
        String email = "joinTest";
        String password = "12345";
        String nickname = "v";

        String apiUrl = "/api/v1/join";

        JoinRequestDto dto = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)   //????????? MockMvc??? ?????? API??? ?????????
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"12345\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void ????????????_??????_?????????_??????() throws Exception{
        //given

        String email = this.dupEmail;
        String password = "1234qwer%T";
        String nickname = "vV?????????VvV";

        String apiUrl = "/api/v1/join";

        JoinRequestDto dto = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"1234qwer%T\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void ????????????_??????_?????????_??????() throws Exception{
        //given
        String email = "joinTest1@withme.com";
        String password = "1234qwer%T";
        String nickname = this.dupNick;

        String apiUrl = "/api/v1/join";

        JoinRequestDto dto = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"1234qwer%T\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError());
    }


}
