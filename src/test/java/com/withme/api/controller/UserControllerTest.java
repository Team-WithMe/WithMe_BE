package com.withme.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.controller.dto.UserUpdateRequestDto;
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

        userController.createUser(dto);
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
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void 회원가입_실패_이메일_중복() throws Exception{
        //given

        String email = this.dupEmail;
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
                .andExpect(status().is4xxClientError());
    }


    @Test
    public void 회원가입_실패_닉네임_중복() throws Exception{
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


    @Test
    public void 닉네임변경_성공() throws Exception{
        //given
        Long id = 1L;
        String nicknameToBeChanged = "vV위드미Vv";

        String apiUrl = "/api/v1/user/nickname/"+id;

        UserUpdateRequestDto dto = UserUpdateRequestDto.builder()
                .id(id)
                .nickname(nicknameToBeChanged)
                .build();

        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)   //생성된 MockMvc를 통해 API를 테스트
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )

                //then
                .andExpect(status().isOk());

        assertThat(userRepository.findById(id)
                .map(User::getNickname)).isEqualTo(nicknameToBeChanged);
    }

}
