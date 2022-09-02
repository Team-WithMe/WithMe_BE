package com.withme.api.team;

import com.withme.api.controller.dto.TeamListResponseMapping;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.TeamRepository;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamSkill.TeamSkillRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
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

    @Transactional
    @DisplayName("팀 리스트 스킬 조회 테스트")
    @Test
    void findTeamListBySkills() throws Exception {
        List<Skill> skillList = new ArrayList<>();
        List<TeamListResponseMapping> teamList = new ArrayList<>();
        List<TeamSkill> teamSkillsParams = new ArrayList<>();
        List<List<TeamSkill>> teamSkillsParamsList = new ArrayList<>();

        // NOTE 검색 받은 SKILL을 Skill List에 저장
        for (SkillName skillName : getSkillNameList()) {
            skillList.add(Skill.builder().skillName(skillName).build());
        }

        // NOTE 검색 조건 사용을 위해 TeamSkill 조회
        List<TeamSkill> teamSkills = teamSkillRepository.findAll();

        // NOTE 검색 조건 걸러냄
        for (Skill skill : skillList) {
            teamSkillsParams = teamSkills.stream()
                    .filter(teamSkill -> {
                        return teamSkill.getSkill().getSkillName().equals(skill.getSkillName());
                    }).collect(Collectors.toList());
            teamSkillsParamsList.add(teamSkillsParams);
        }

        // NOTE 검색 조건을 위해 리스트를 합침
        List<TeamSkill> params = teamSkillsParamsList.stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        if (teamSkills.size() <= 0){
            teamList = teamRepository.findAllByStatusOrderByCreatedTimeDesc(Status.DISPLAYED).orElseThrow(
                    () -> new Exception("팀 조회 오류 (검색X)")
            );
        }else{
            teamList = teamRepository.findTeamsByTeamSkillsInAndStatusOrderByCreatedTimeDesc(params, Status.DISPLAYED).orElseThrow(
                    () -> new Exception("팀 조회 오류 (검색O)")
            );
        }

        List<SkillName> equalsSkillName = new ArrayList<>();
        for (TeamListResponseMapping teamListResponseMapping : teamList) {
            for (TeamSkill teamSkill : teamListResponseMapping.getTeamSkills()) {
                SkillName skillName = teamSkill.getSkill().getSkillName();
                equalsSkillName.add(skillName);
            }
        }

        equalsSkillName.stream()
                .distinct()
                .collect(Collectors.toList());

        List<SkillName> skillNameList = skillList.stream()
                .map(v -> v.getSkillName())
                .distinct()
                .collect(Collectors.toList());

        // NOTE 검색 결과의 SkillName 리스트가 검색 조건의 SkillName 리스트의 요소가 포함되어 있으면
        Assertions.assertThat(equalsSkillName).containsAll(skillNameList);
    }

    // NOTE 검색 조건
    static List<SkillName> getSkillNameList() {
        List<SkillName> skills = new ArrayList<>();
        skills.add(SkillName.java);
        skills.add(SkillName.nodejs);

        return skills;
    }

}
