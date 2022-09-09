package com.withme.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.controller.dto.TeamNoticeCreateRequestDto;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.team.TeamRepository;
import com.withme.api.domain.teamNotice.TeamNotice;
import com.withme.api.domain.teamNotice.TeamNoticeRepository;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
import com.withme.api.domain.teamUser.TeamUserRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private TeamNoticeRepository teamNoticeRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String jwtToken;


    @BeforeEach
    public void setup() throws Exception {
        this.mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    public void tearDown() {
        teamUserRepository.deleteAll();
        teamNoticeRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    public void 공지사항등록_성공() throws Exception{
        //given
        String testEmail = "123#123.com";
        String testPw = "1!2@3#4$5%";
        String testNick = "Shawn";

        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email(testEmail)
                .password(testPw)
                .nickname(testNick)
                .build();

        userController.createUser(joinRequestDto);

        this.jwtToken = this.mvc.perform(post("http://localhost:"+port+ "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + testEmail + "\"" +
                                ",\"password\":\"" + testPw + "\"" +
                                "}"
                        ))
                .andReturn()
                .getResponse()
                .getHeaderValue(tokenProvider.AUTHORIZATION_HEADER)
                .toString();

        User user1 = userRepository.findByNickname(testNick)
                .orElseThrow();

        Team team1 = Team.builder()
                .teamName("네트워크 공부하기")
                .teamCategory(TeamCategory.STUDY)
                .teamDesc("매주 주말에 카페에 모여 네트워크를 공부는 스터디 모임입니다.")
                .status(Status.DISPLAYED)
                .build();

        TeamUser teamUser1 = TeamUser.builder()
                .memberType(MemberType.LEADER)
                .team(team1)
                .user(user1)
                .build();

//        user1.getUserTeams().add(teamUser1);
//        team1.addNewUser(teamUser1);
        teamRepository.saveAndFlush(team1);

        String title = "모임시간 공지";
        String content = "모임은 매주 일요일 오후 1시에 사거리 카페에서 합니다.";

        TeamNoticeCreateRequestDto dto = TeamNoticeCreateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        String apiUrl = "/api/v1/team/" + team1.getId() + "/notice";
        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)
                        .header(tokenProvider.AUTHORIZATION_HEADER, this.jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                //then
                .andExpect(status().isCreated());

        assertThat(teamNoticeRepository.findById(1L)
                .map(TeamNotice::getTitle)).isEqualTo(Optional.of(title));
    }

    @Test
    @Transactional
    public void 공지사항등록_실패_팀리더아님() throws Exception{
        //given
        String testEmail = "123#123.com";
        String testPw = "1!2@3#4$5%";
        String testNick = "Shawn";

        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email(testEmail)
                .password(testPw)
                .nickname(testNick)
                .build();

        userController.createUser(joinRequestDto);

        this.jwtToken = this.mvc.perform(post("http://localhost:"+port+ "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + testEmail + "\"" +
                                ",\"password\":\"" + testPw + "\"" +
                                "}"
                        ))
                .andReturn()
                .getResponse()
                .getHeaderValue(tokenProvider.AUTHORIZATION_HEADER)
                .toString();

        User user1 = userRepository.findByNickname(testNick)
                .orElseThrow();

        Team team1 = Team.builder()
                .teamName("네트워크 공부하기")
                .teamCategory(TeamCategory.STUDY)
                .teamDesc("매주 주말에 카페에 모여 네트워크를 공부는 스터디 모임입니다.")
                .status(Status.DISPLAYED)
                .build();
        Team team2 = Team.builder()
                .teamName("OS 공부하기")
                .teamCategory(TeamCategory.PROJECT)
                .teamDesc("카페에 모여 토이프로젝트를 진행합니다.")
                .status(Status.DISPLAYED)
                .build();

        TeamUser teamUser1 = TeamUser.builder()
                .memberType(MemberType.LEADER)
                .team(team1)
                .user(user1)
                .build();

//        user1.getUserTeams().add(teamUser1);
//        team1.addNewUser(teamUser1);
        teamRepository.saveAndFlush(team2);

        String title = "모임시간 공지";
        String content = "모임은 매주 일요일 오후 1시에 사거리 카페에서 합니다.";

        TeamNoticeCreateRequestDto dto = TeamNoticeCreateRequestDto.builder()
                .title(title)
                .content(content)
                .build();

        String apiUrl = "/api/v1/team/" + team2.getId() + "/notice";
        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(post(url)
                        .header(tokenProvider.AUTHORIZATION_HEADER, this.jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("This User is not a Leader of this Team."));
    }

    @Test
    @Transactional
    public void 공지사항조회_성공() throws Exception {
        //given
        String testEmail = "123#123.com";
        String testPw = "1!2@3#4$5%";
        String testNick = "Shawn";

        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email(testEmail)
                .password(testPw)
                .nickname(testNick)
                .build();

        userController.createUser(joinRequestDto);

        this.jwtToken = this.mvc.perform(post("http://localhost:"+port+ "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"email\":\"" + testEmail + "\"" +
                                ",\"password\":\"" + testPw + "\"" +
                                "}"
                        ))
                .andReturn()
                .getResponse()
                .getHeaderValue(tokenProvider.AUTHORIZATION_HEADER)
                .toString();

        User user1 = userRepository.findByNickname(testNick)
                .orElseThrow();

        Team team1 = Team.builder()
                .teamName("네트워크 공부하기")
                .teamCategory(TeamCategory.STUDY)
                .teamDesc("매주 주말에 카페에 모여 네트워크를 공부는 스터디 모임입니다.")
                .status(Status.DISPLAYED)
                .build();

        TeamUser teamUser1 = TeamUser.builder()
                .team(team1)
                .user(user1)
                .memberType(MemberType.MEMBER)
                .build();

//        user1.getUserTeams().add(teamUser1);
//        team1.addNewUser(teamUser1);
        teamRepository.saveAndFlush(team1);

        String title1 = "title1";
        String content1 = "content1";
        String title2 = "title2";
        String content2 = "content2";

        TeamNotice teamNotice1 = TeamNotice.builder()
                .title(title1)
                .content(content1)
                .team(team1)
                .writer(user1)
                .build();
        TeamNotice teamNotice2 = TeamNotice.builder()
                .title(title2)
                .content(content2)
                .team(team1)
                .writer(user1)
                .build();

        String apiUrl = "/api/v1/team/" + team1.getId() + "/notice";
        String url = "http://localhost:" + port + apiUrl;

        //when
        mvc.perform(get(url)
                        .header(tokenProvider.AUTHORIZATION_HEADER, this.jwtToken)
                )
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(teamNotice1.getId()))
                .andExpect(jsonPath("$[0].title").value(teamNotice1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(teamNotice1.getContent()))
                .andExpect(jsonPath("$[1].id").value(teamNotice2.getId()))
                .andExpect(jsonPath("$[1].title").value(teamNotice2.getTitle()))
                .andExpect(jsonPath("$[1].content").value(teamNotice2.getContent()))
        ;
    }

}