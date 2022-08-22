package com.withme.api.controller.dto;

import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "팀 정보 응답 DTO 객체")
@Getter
@NoArgsConstructor
public class TeamResponseDto {

    @Schema(description = "팀 id", example = "1")
    private Long id;

    @Schema(description = "팀 이름", example = "별다방 스터디 모임")
    private String teamName;

    @Schema(description = "팀 구분", example = "STUDY or PROJECT")
    private TeamCategory teamCategory;

    @Schema(description = "팀 설명", example = "매주 일요일에 별다방에서 자유주제 스터디를 진행합니다.")
    private String teamDesc;

    @Schema(description = "공개 여부", example = "DISPLAYED or HIDDEN")
    private Status status;

    public TeamResponseDto(Team team) {
        this.id = team.getId();
        this.teamName = team.getTeamName();
        this.teamCategory = team.getTeamCategory();
        this.teamDesc = team.getTeamDesc();
        this.status = team.getStatus();
    }

}
