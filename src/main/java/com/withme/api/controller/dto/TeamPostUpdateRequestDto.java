package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "팀 게시물 제목, 내용 업데이트 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
public class TeamPostUpdateRequestDto {

    @Schema(description = "팀 게시물 제목", example = "같이 스터디 하실분?", required = true)
    @Size(min = 1, max = 100, message = "팀 게시물 제목은 100글자 이하입니다.")
    @NotNull // NOTE NotNull이 아닐경우 오류
    private String title;

    @Schema(description = "팀 게시물 내용", example = "토이 프로젝트하실 분은 댓글로 남겨주세요", required = true)
    @Size(min = 1, max = 1000, message = "팀 게시물 내용은 1000글자 이하입니다.")
    @NotNull // NOTE NotNull이 아닐경우 오류
    private String content;

}
