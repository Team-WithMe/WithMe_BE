package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.teamUser.TeamUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    @Schema(description = "팀 댓글 카운트", example = "10")
    private Integer commentCount;

    @Schema(description = "팀 스킬", example = "{'java', 'mysql'}")
    private List<SkillName> teamSkills;

    @Schema(description = "팀 댓글", example = "{'comment' : {}")
    private List<TeamCommentResponseDto> teamComments;

    @Schema(description = "팀 게시물 작성자 id", example = "userId")
    private Long teamUserid;

    @Schema(description = "팀 게시물 작성자 닉네임", example = "닉네임")
    private String teamUserNickName;

    @Schema(description = "팀 게시물 등록 날짜", example = "2022-01-01 14:21:12")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    @Schema(description = "팀 게시물 수정 날짜", example = "2022-01-01 14:21:12")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    public TeamDetailResponseDto(Team team, List<TeamCommentResponseDto> teamComments, TeamUser teamUser) {
        this.id = team.getId();
        this.title = team.getTitle();
        this.content = team.getContent();
        this.teamName = team.getTeamName();
        this.teamDesc = team.getTeamDesc();
        this.teamCategory = team.getTeamCategory();
        this.viewCount = team.getViewCount();
        this.commentCount = team.getCommentCount();
        this.teamComments = teamComments;
        this.teamUserid = teamUser.getUser().getId();
        this.teamUserNickName = teamUser.getUser().getNickname();
        this.teamSkills = team.getTeamSkillNameList();
        // NOTE 없을 수 있는 값
        if (team.getCreatedTime() == null){
            this.createDate = null;
        }else {
            this.createDate = team.getCreatedTime();
        }
        if (team.getModifiedTime() == null){
            this.updateDate = null;
        }else {
            this.updateDate = team.getModifiedTime();
        }
    }
}
