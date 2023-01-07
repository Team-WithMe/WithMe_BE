package com.withme.api.controller.dto;

import com.withme.api.domain.team.Team;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "팀 추천 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamDetailRecommendReaponseDto {

    @Schema(description = "팀 id ", example = "1")
    private Long id;

    @Schema(description = "팀 제목 ", example = "같이 팀프로젝트 하실분")
    private String title;

    public TeamDetailRecommendReaponseDto(Team team) {
        this.id = team.getId();
        this.title = team.getTitle();
    }
}
