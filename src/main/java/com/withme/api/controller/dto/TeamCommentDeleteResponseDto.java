package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "팀 댓글 삭제 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamCommentDeleteResponseDto {

    @Schema(description = "응답 상태", example = "201", required = true)
    private Integer status;

    @Schema(description = "팀 id", example = "1", required = true)
    private Long teamId;

    @Schema(description = "팀 댓글 id", example = "1", required = true)
    private Long commentId;

    public TeamCommentDeleteResponseDto(Integer status, Long teamId, Long commentId) {
        this.status = status;
        this.teamId = teamId;
        this.commentId = commentId;
    }
}
