package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "댓글 좋아요 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class CommentLikeRequestDto {

    @Schema(description = "댓글 id ", example = "1")
    private Long commentId;

}
