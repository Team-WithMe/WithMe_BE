package com.withme.api.domain;

import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.team.TeamRepository;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
import com.withme.api.domain.teamUser.TeamUserRepository;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import com.withme.api.service.TeamService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
class TeamTest {

    @Autowired
    TeamService teamService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TeamUserRepository teamUserRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        teamRepository.deleteAll();
        teamUserRepository.deleteAll();
    }

    @Test
    @Transactional
    void isUserJoined() {
        User user1 = User.builder()
                .nickname("테스트")
                .userImage("default")
                .role("ROLE_USER")
                .joinRoot("withMe")
                .build();

        userRepository.saveAndFlush(user1);

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

        user1.getUserTeams().add(teamUser1);
        team1.newUserJoined(teamUser1);

        assertThat(team1.IsUserTeamMember(user1.getId())).isTrue();
        assertThat(team2.IsUserTeamMember(user1.getId())).isFalse();

//        assertThatThrownBy(() -> team2.IsUserTeamMember(user1.getId()))
//                .isInstanceOf(RuntimeException.class)
//                        .hasMessageContaining("This User is not a Member");
    }

    @Test
    void isUserLeader() {
        User user1 = User.builder()
                .nickname("테스트")
                .userImage("default")
                .role("ROLE_USER")
                .joinRoot("withMe")
                .build();

        userRepository.saveAndFlush(user1);

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

        user1.getUserTeams().add(teamUser1);
        team1.newUserJoined(teamUser1);

        user1.joinTeam(team2);

        assertThat(team1.IsUserTeamLeader(user1.getId())).isTrue();
        assertThat(team2.IsUserTeamLeader(user1.getId())).isFalse();

    }
}