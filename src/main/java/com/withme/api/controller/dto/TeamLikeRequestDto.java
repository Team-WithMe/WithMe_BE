package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "팀 추천 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamLikeRequestDto {

    @Schema(description = "팀 id ", example = "1")
    private Long teamId;

    @Schema(description = "팀 id ", example = "1")
    private Long userId;
}
