package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "팀 댓글 수정 요청 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamCommentModifyRequestDto {

    @Schema(description = "팀 댓글 id", example = "1")
    private Long commentId;

    @Schema(description = "팀 댓글 작성자 id", example = "12")
    private Long teamUserId;

    @Size(min = 1, max = 1000, message = "팀 댓글 내용은 1000글자 이하입니다.")
    @NotNull // NOTE NotNull이 아닐경우 오류
    @Schema(description = "팀 댓글 수정 내용", example = "댓글내용입니다.")
    private String content;

}
