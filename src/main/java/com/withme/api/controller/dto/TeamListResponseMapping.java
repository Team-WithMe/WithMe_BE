package com.withme.api.controller.dto;

import com.withme.api.domain.skill.Skill;

import java.util.List;

public interface TeamListResponseMapping {
    Long getTeamIdx();

    String getTeamName();

    String getTeamDesc();

    List<Skill> getSkills();

    int getTeamListCount();
}
