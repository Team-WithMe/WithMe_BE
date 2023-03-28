package com.withme.api.controller.dto;

import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamUser.TeamUser;

import java.time.LocalDateTime;
import java.util.List;

public interface TeamListResponseMapping {
    Long getId();

    String getTitle();
    String getTeamName();
    String getTeamDesc();
    List<TeamSkill> getTeamSkills();
    LocalDateTime getCreatedTime();
    LocalDateTime getModifiedTime();

}
