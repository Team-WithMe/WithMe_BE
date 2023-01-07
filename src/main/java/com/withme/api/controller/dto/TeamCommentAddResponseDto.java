package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Schema(description = "팀 게시물 댓글, 추가 응답 DTO")
@Getter
@Setter
@NoArgsConstructor
public class TeamCommentAddResponseDto {

    @Schema(description = "응답 상태", example = "201", required = true)
    private Integer status;

    @Schema(description = "팀 id", example = "1", required = true)
    private Long teamId;

    public TeamCommentAddResponseDto(Integer status, Long teamId) {
        this.status = status;
        this.teamId = teamId;
    }
}
