package com.withme.api.controller;


import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import com.withme.api.jwt.TokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
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

    @Autowired
    private TokenProvider tokenProvider;

    private MockMvc mvc;

    private final String setupEmail = "set@up.com";
    private final String setupNick = "setNick";

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        userRepository.findAll().forEach(user -> userRepository.delete(user));
    }


    @Test
    public void 회원가입_성공() throws Exception{
        //given
        String email = "joinTest@withme.com";
        String password = "1234qwer%T";
        String nickname = "vV위드미Vv";

        String apiUrl = "/api/v1/join";

        JoinRequestDto dto = JoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build();

        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)   //생성된 MockMvc를 통해 API를 테스트
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"1234qwer%T\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

        //then
                .andExpect(status().isCreated());

        assertThat(userRepository.findByEmailAndPasswordIsNotNull(email)
                .map(User::getNickname)).isEqualTo(Optional.of(nickname));
    }

    @Test
    public void 회원가입_실패_유효성_부적합() throws Exception{
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
        mvc.perform(post(url)   //생성된 MockMvc를 통해 API를 테스트
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + dto.getEmail() + "\"" +
                                ",\"password\":\"12345\"" +
                                ",\"nickname\":\"" + dto.getNickname() + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Validation Failed"));


    }


    @Test
    public void 회원가입_실패_이메일_중복() throws Exception{
        //given
        JoinRequestDto alreadyJoined = JoinRequestDto.builder()
                .email(this.setupEmail)
                .password("1234qwer%T")
                .nickname(this.setupNick)
                .build();
        userController.createUser(alreadyJoined);

        String email = this.setupEmail;
        String password = "1234qwer%T";
        String nickname = "vV위드미VvV";

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
                .andExpect(status().is4xxClientError())
//                .andExpect(content().json("{\"message\": \"Email Duplicated\"}"));
                .andExpect(jsonPath("$.message").value("Email Duplicated"));


    }


    @Test
    public void 회원가입_실패_닉네임_중복() throws Exception{
        //given
        JoinRequestDto alreadyJoined = JoinRequestDto.builder()
                .email(this.setupEmail)
                .password("1234qwer%T")
                .nickname(this.setupNick)
                .build();
        userController.createUser(alreadyJoined);

        String email = "joinTest1@withme.com";
        String password = "1234qwer%T";
        String nickname = this.setupNick;

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
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Nickname Duplicated"));

    }

}
