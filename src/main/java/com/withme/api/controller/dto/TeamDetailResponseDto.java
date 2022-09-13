package com.withme.api.controller.dto;

import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.teamComment.TeamComment;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamUser.TeamUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "팀 게시물 상세정보 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamDetailResponseDto {

    @Schema(description = "팀 id", example = "1")
    private Long id;

    @Schema(description = "팀 게시물 제목", example = "스터디 모임")
    private String title;

    @Schema(description = "팀 게시물 내용", example = "스터디입니다. 댓글로 달아주세요")
    private String content;

    @Schema(description = "팀 이름", example = "withMe")
    private String teamName;

    @Schema(description = "팀 설명", example = "스터디를 위한 팀입니다.")
    private String teamDesc;

    @Schema(description = "팀 카테고리", example = "PROJECT")
    private TeamCategory teamCategory;

    @Schema(description = "팀 게시물 조회수", example = "1")
    private Integer viewCount;

    @Schema(description = "팀 스킬", example = "{'java', 'mysql'}")
    private List<SkillName> teamSkills;

    @Schema(description = "팀 댓글", example = "{'comment' : {}")
    private List<TeamComment> teamComments;

    @Schema(description = "팀 게시물 작성자 id", example = "userId")
    private Long teamUserid;

    @Schema(description = "팀 게시물 작성자 닉네임", example = "닉네임")
    private String teamUserNickName;

    public TeamDetailResponseDto(Team team, List<TeamComment> teamComments, TeamUser teamUser) {
        this.id = team.getId();
        this.title = team.getTitle();
        this.content = team.getContent();
        this.teamName = team.getTeamName();
        this.teamDesc = team.getTeamDesc();
        this.teamCategory = team.getTeamCategory();
        this.viewCount = team.getViewCount();
        this.teamComments = teamComments;
        this.teamUserid = teamUser.getUser().getId();
        this.teamUserNickName = teamUser.getUser().getNickname();
        this.teamSkills = team.getTeamSkillNameList();
    }
}
