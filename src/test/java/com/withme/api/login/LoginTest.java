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
    String username = "위드미 테스트";
    String password = "12345";
    String nickname = "vV위드미짱짱Vv";

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();

        //로그인할 계정 회원가입
        JoinRequestDto dto = JoinRequestDto.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .build();

        userController.join(dto);
    }

    @AfterEach
    public void tearDown(){
        userRepository.delete(userRepository.findByEmail(this.email));
    }

    @Test
    public void 로그인() throws Exception{
        String url = "http://localhost:" + port + "/login";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\":\"" + this.username + "\"" +
                                ",\"password\":\"" + this.password + "\"" +
                                "}"
                        ))

        //then
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"));
    }

}
