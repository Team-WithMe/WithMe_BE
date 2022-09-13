package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "팀 게시물 댓글, 추가 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
public class TeamCommentAddRequestDto {

    @Schema(description = "팀 댓글 내용", example = "댓글입니다.", required = true)
    @Size(min = 1, max = 1000, message = "팀 댓글 내용은 1000글자 이하입니다.")
    @NotNull // NOTE NotNull이 아닐경우 오류
    private String content;
}
