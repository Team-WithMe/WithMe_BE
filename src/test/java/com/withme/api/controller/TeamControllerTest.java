package com.withme.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.withme.api.controller.dto.TeamNoticeCreateRequestDto;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
import com.withme.api.domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;


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
    @WithMockUser(roles = "USER")
    public void 공지사항등록_성공() throws Exception{
        //given
        Team team1 = Team.builder()
                .teamName("네트워크 공부하기")
                .teamCategory(TeamCategory.STUDY)
                .teamDesc("매주 주말에 카페에 모여 네트워크를 공부는 스터디 모임입니다.")
                .status(Status.DISPLAYED)
                .build();
        User user1 = User.builder()
                .nickname("승현")
                .userImage("default")
                .role("ROLE_USER")
                .joinRoot("WithMe")
                .build();
        TeamUser teamUser1 = TeamUser.builder()
                .memberType(MemberType.LEADER)
                .team(team1)
                .user(user1)
                .build();

        TeamNoticeCreateRequestDto dto = TeamNoticeCreateRequestDto.builder()
                .title("모임시간 공지")
                .content("모임은 매주 일요일 오후 1시에 사거리 카페에서 합니다.")
//                .team(team1)
//                .writer(user1)
                .build();

        String apiUrl = "/api/v1/team/" + team1.getId() + "/notice";
        String url = "http://localhost:" + port + apiUrl;

        String expectedTitle = "$.[?(@.title == '%s')]";
        String expectedContent = "$.[?(@.content == '%s')]";
        String expectedCreatedTime = "$..[?(@.createdTime == '%s')]";

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
                )
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath(expectedTitle, dto.getTitle()).exists())
                .andExpect(jsonPath(expectedContent, dto.getContent()).exists())
                .andExpect(jsonPath(expectedCreatedTime, team1.getCreatedTime()).exists())
                ;

    }

}
