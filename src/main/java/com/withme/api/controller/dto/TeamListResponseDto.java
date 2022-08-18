package com.withme.api.controller.dto;

import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamUser.TeamUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class TeamListResponseDto {

    Long getId;

    String getTeamName;

//    TeamCategory teamCategory;
//
//    Status status;

    String getTeamDesc;

//    TeamSkill getTeamSkill;

//
//    List<TeamUser> teamUsers;

}
