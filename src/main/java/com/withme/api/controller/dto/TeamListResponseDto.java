package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamUser.TeamUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "팀 리스트 정보 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamListResponseDto {

    @Schema(description = "팀 id", example = "1")
    private Long id;

    @Schema(description = "팀 게시물 제목", example = "스터디 모임")
    private String title;

    @Schema(description = "팀 이름", example = "withMe")
    private String teamName;

    @Schema(description = "팀 카테고리", example = "PROJECT")
    private TeamCategory teamCategory;

    @Schema(description = "팀 게시물 등록 날짜", example = "2022-01-01 14:21:12")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    @Schema(description = "팀 게시물 수정 날짜", example = "2022-01-01 14:21:12")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    @Schema(description = "팀 스킬", example = "{'java', 'mysql'}")
    private List<SkillName> teamSkills;

    @Schema(description = "팀 게시물 조회수", example = "1")
    private Integer viewCount;

    @Schema(description = "팀 댓글 카운트", example = "10")
    private Integer commentCount;

    public TeamListResponseDto toTeamListResponseDto(Team team) {
        this.id = team.getId();
        this.title = team.getTitle();
        this.teamName = team.getTeamName();
        this.teamCategory = team.getTeamCategory();
        this.createDate = team.getCreatedTime();
        this.updateDate = team.getModifiedTime();
        this.teamSkills = team.getTeamSkillNameList();
        this.viewCount = team.getViewCount();
        this.commentCount = team.getCommentCount();
        return this;
    }
}
