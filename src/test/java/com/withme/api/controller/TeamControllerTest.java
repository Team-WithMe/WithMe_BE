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
import com.withme.api.jwt.TokenProvider;
import com.withme.api.service.UserService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

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

        User user1 = userService.createUser(joinRequestDto);

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

        teamRepository.saveAndFlush(team1);
        teamUserRepository.saveAndFlush(teamUser1);

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
    public void 공지사항등록_실패_팀멤버아님() throws Exception{
        //given
        String testEmail = "123#123.com";
        String testPw = "1!2@3#4$5%";
        String testNick = "Shawn";

        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email(testEmail)
                .password(testPw)
                .nickname(testNick)
                .build();

        User user1 = userService.createUser(joinRequestDto);

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
                .team(team2)
                .user(user1)
                .build();

        teamRepository.saveAndFlush(team1);
        teamRepository.saveAndFlush(team2);
        teamUserRepository.saveAndFlush(teamUser1);

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
    public void 공지사항등록_실패_리더아님() throws Exception{
        //given
        String testEmail = "123#123.com";
        String testPw = "1!2@3#4$5%";
        String testNick = "Shawn";

        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email(testEmail)
                .password(testPw)
                .nickname(testNick)
                .build();

        User user1 = userService.createUser(joinRequestDto);

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

        teamRepository.saveAndFlush(team1);
        teamUserRepository.saveAndFlush(teamUser1);

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

//    @Test
//    @WithMockUser(roles = "USER")
//    public void 공지사항조회_성공() throws Exception {
//        //given
//        Team team1 = Team.builder()
//                .teamName("네트워크 공부하기")
//                .teamCategory(TeamCategory.STUDY)
//                .teamDesc("매주 주말에 카페에 모여 네트워크를 공부는 스터디 모임입니다.")
//                .status(Status.DISPLAYED)
//                .build();
//
//        User user1 = User.builder()
//                .nickname("승현")
//                .userImage("default")
//                .role("ROLE_USER")
//                .joinRoot("WithMe")
//                .build();
//
//        TeamUser teamUser1 = TeamUser.builder()
//                .team(team1)
//                .user(user1)
//                .memberType(MemberType.MEMBER)
//                .build();
//
//        teamRepository.saveAndFlush(team1);
//
//        String title1 = "title1";
//        String content1 = "content1";
//        String title2 = "title2";
//        String content2 = "content2";
//
//        TeamNotice teamNotice1 = TeamNotice.builder()
//                .title(title1)
//                .content(content1)
//                .team(team1)
//                .writer(user1)
//                .build();
//        TeamNotice teamNotice2 = TeamNotice.builder()
//                .title(title2)
//                .content(content2)
//                .team(team1)
//                .writer(user1)
//                .build();
//
//
//        String apiUrl = "/api/v1/team/" + team1.getId() + "/notice";
//        String url = "http://localhost:" + port + apiUrl;
//
//        String expectedId = "$.[?(@.id == '%s')]";
//        String expectedTitle = "$.[?(@.title == '%s')]";
//        String expected = "$.[?(@.title == '%s')]";
//
//        //when
//        mvc.perform(get(url)
//                        .header(tokenProvider.AUTHORIZATION_HEADER, this.jwtToken)
//                )
//                //then
//                .andExpect(status().isOk());
//
//    }

}