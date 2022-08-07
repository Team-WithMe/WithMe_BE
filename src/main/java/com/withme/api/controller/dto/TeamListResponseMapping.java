package com.withme.api.controller.dto;

import com.withme.api.domain.skill.Skill;

import java.util.Set;

public interface TeamListResponseMapping {
    Long getTeamIdx();

    String getTeamName();

    String getTeamDesc();

    Set<Skill> getSkills();
}
