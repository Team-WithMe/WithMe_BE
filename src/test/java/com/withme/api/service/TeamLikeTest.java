package com.withme.api.service;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamRepository;
import com.withme.api.domain.teamLike.TeamLike;
import com.withme.api.domain.teamLike.TeamLikeRepository;
import com.withme.api.domain.teamSkill.TeamSkillRepository;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamLikeTest {

    @Autowired
    private TeamLikeRepository teamLikeRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @DisplayName("팀 좋아요 등록, 취소 기능")
    @Test
    void 팀_좋아요_기능() {
        String result = "";
        Long teamId = 70L;
        Long userId = 1L;

        // NOTE Entity 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NullPointerException("not found team"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("not found user"));

        // NOTE 사용자가 해당 팀에 좋아요를 이미 눌렀는지 확인
        TeamLike teamLike = teamLikeRepository.countTeamLikesByTeamAndUser(teamId, userId)
                .orElseGet(() -> new TeamLike());

        // NOTE 이미 좋아요 있음 (좋아요 취소)
        if (teamLike.getId() != null){
            teamLikeRepository.deleteById(teamLike.getId());
            result = "cancel";
        // NOTE 좋아요 없음 (좋아요 등록)
        }else {
            teamLikeRepository.save(new TeamLike(team, user));
            result = "register";
        }

        Assertions.assertThat(result).isEqualTo("register");
    }
}
