package com.withme.api.service;

import com.withme.api.controller.dto.CreateTeamRequestDto;
import com.withme.api.controller.dto.TeamListResponseMapping;
import com.withme.api.controller.dto.TeamSearchDto;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.team.TeamRepository;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamSkill.TeamSkillRepository;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamListTest {
    @Autowired
    private TeamSkillRepository teamSkillRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @DisplayName("팀 리스트 스킬 조회 테스트")
    @Test
    void findTeamListBySkills() throws Exception {
        // NOTE 검색 DTO 가져오기
        TeamSearchDto teamSearchDto = getTeamSearchDto();
        // NOTE 검색 받은 SKILL을 Skill List에 저장
        List<Skill> skillList = teamSearchDto.toSkillList();

        List<TeamListResponseMapping> teamList = new ArrayList<>();

        List<TeamSkill> teamSkills = teamSkillRepository.findAll();
        List<TeamSkill> teamSkillList = toTeamListParams(skillList, teamSkills);

        if (teamSkillList.size() == 0 && skillList.size() == 0){
            teamList = teamRepository.findAllByOrderByCreatedTimeDesc().orElseThrow(
                    () -> new NullPointerException("팀 조회 오류 (검색X)")
            );
        }else{
            teamList = teamRepository.findDistinctTeamsByTeamSkillsInOrderByCreatedTimeDesc(teamSkillList).orElseThrow(
                    () -> new NullPointerException("팀 조회 오류 (검색O)")
            );
        }

        List<SkillName> equalsSkillName = new ArrayList<>();
        for (TeamListResponseMapping teamListResponseMapping : teamList) {
            for (TeamSkill teamSkill : teamListResponseMapping.getTeamSkills()) {
                SkillName skillName = teamSkill.getSkill().getSkillName();
                equalsSkillName.add(skillName);
            }
        }

        List<SkillName> skillNameList = skillList.stream()
                .distinct()
                .map(v -> v.getSkillName())
                .collect(Collectors.toList());

        // NOTE 검색 결과의 SkillName 리스트가 검색 조건의 SkillName 리스트의 요소가 포함되어 있으면
        Assertions.assertThat(equalsSkillName).containsAll(skillNameList);
    }

    // NOTE 검색 조건
    static List<SkillName> getSkillNameList() {
        List<SkillName> skills = new ArrayList<>();
        skills.add(SkillName.java);
        skills.add(SkillName.nodejs);
        skills.add(SkillName.docker);

        return skills;
    }

    TeamSearchDto getTeamSearchDto() {
        List<SkillName> skillNames = new ArrayList<>();
        skillNames.add(SkillName.docker);
        skillNames.add(SkillName.java);
        return TeamSearchDto.builder()
                .skills(skillNames)
                .sort(0)
                .build();
    }

    // NOTE 팀 검색 조건 처리 로직
    public List<TeamSkill> toTeamListParams(List<Skill> skills, List<TeamSkill> teamSkills) {
        List<TeamSkill> teamSkillsParams = new ArrayList<>();
        List<List<TeamSkill>> teamSkillsParamsList = new ArrayList<>();
        // NOTE 검색 조건 걸러냄
        for (Skill skill : skills) {
            teamSkillsParams = teamSkills.stream()
                    .filter(teamSkill -> {
                        return teamSkill.getSkill().getSkillName().equals(skill.getSkillName());
                    })
                    .collect(Collectors.toList());
            teamSkillsParamsList.add(teamSkillsParams);
        }

        // NOTE 검색 조건을 위해 리스트를 합침
        List<TeamSkill> params = teamSkillsParamsList.stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        return params;
    }
    @Transactional
    @DisplayName("팀 등록 테스트")
    @Test
    void 팀등록_테스트() {
        // NOTE 팀 등록 DTO 가져오기
        CreateTeamRequestDto teamRequestDto = getTeamRequestDto();
        // NOTE 팀 DTO 팀 변환
        Team team = teamRequestDto.setTeamSkill();

        Long user_idx = 1L;

        User user = userRepository.findById(user_idx).orElseThrow(
                ()-> new NullPointerException("존재하지않는 사용자"));
        System.out.println("user = " + user);

        // NOTE 팀등록
        TeamUser teamUser = TeamUser.builder()
                .memberType(MemberType.LEADER)
                .team(team)
                .user(user)
                .build();
        team.addTeamUser(teamUser);
        team.getTeamUsers().stream().forEach(v -> System.out.println(v.getUser().getId()));
        Team saveTeam = teamRepository.save(team);

        Assertions.assertThat(saveTeam.getTeamName()).isEqualTo("팀이름111");
    }

    CreateTeamRequestDto getTeamRequestDto() {
        List<String> skillNames = new ArrayList<>();
        skillNames.add("docker");
        skillNames.add("java");

        return CreateTeamRequestDto.builder()
                .category(TeamCategory.PROJECT)
                .skills(skillNames)
                .description("팅 설명111")
                .name("팀이름111")
                .build();
    }
}
