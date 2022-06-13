package com.withme.api.login;

import com.withme.api.controller.UserController;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest{

    @LocalServerPort
    private int port;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    //given
    String email = "joinTest@withme.com";
    String password = "1234qwer%T";
    String nickname = "vV위드미Vv";

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        //로그인할 계정 회원가입
        JoinRequestDto dto = JoinRequestDto.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();

        userController.join(dto);
    }

    @AfterEach
    public void tearDown(){
        userRepository.delete(userRepository.findByEmail(this.email)
                .orElseThrow(() -> new UsernameNotFoundException(this.email + "not exist.")));
    }

    @Test
    public void 로그인_성공() throws Exception{
        String url = "http://localhost:" + port + "/login";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + this.email + "\"" +
                                ",\"password\":\"" + this.password + "\"" +
                                "}"
                        ))

        //then
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

    @Test
    public void 로그인_실패_잘못된_비밀번호() throws Exception{
        String url = "http://localhost:" + port + "/login";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + this.email + "\"" +
                                ",\"password\":\"" + this.password+"x" + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void 로그인_실패_잘못된_이메일() throws Exception{
        String url = "http://localhost:" + port + "/login";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + this.email + "x" + "\"" +
                                ",\"password\":\"" + this.password + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void 로그인_실패_유효성_부적합() throws Exception{
        String url = "http://localhost:" + port + "/login";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + "email" + "\"" +
                                ",\"password\":\"" + this.password + "\"" +
                                "}"
                        ))

                //then
                .andExpect(status().is4xxClientError());
    }

}
