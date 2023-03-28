package com.withme.api.controller.dto;

import com.withme.api.domain.teamComment.TeamComment;
import com.withme.api.domain.teamSkill.TeamSkill;

import java.time.LocalDateTime;
import java.util.List;

public interface TeamDetailResponseMapping {

    Long getId();

    String getTitle();

    String getContent();

    String getTeamName();

    String getTeamDesc();

    List<TeamSkill> getTeamSkills();

    LocalDateTime getCreatedTime();

    LocalDateTime getModifiedTime();

    List<TeamComment> getComments();

}
