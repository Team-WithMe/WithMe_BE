package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "팀 댓글 삭제 요청 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamCommentDeleteRequestDto {

    @Schema(description = "팀 댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "팀 댓글 작성자 id", example = "12")
    private Long teamUserId;
}
