package com.withme.api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.withme.api.domain.teamComment.TeamComment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "팀 댓글 응답 DTO 객체")
@Getter
@Setter
@NoArgsConstructor
public class TeamCommentResponseDto {
    @Schema(description = "팀 부모 댓글 id", example = "1")
    private Long parentId;

    @Schema(description = "팀 댓글 id", example = "1")
    private Long id;

    @Schema(description = "팀 댓글 내용", example = "댓글입니다.")
    private String content;

    @Schema(description = "팀 댓글 작성자 id", example = "userId")
    private Long teamUserid;

    @Schema(description = "팀 댓글 작성자 닉네임", example = "닉네임")
    private String teamUserNickName;

    @Schema(description = "팀 댓글 좋아요 카운트")
    private Integer commentLikeCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updateDate;

    @Schema(description = "팀 대댓글", example = "대댓글")
    private List<TeamChildrenCommentResponse> commentChildren;

    public TeamCommentResponseDto(TeamComment teamComment) {
        if (teamComment.getParent() == null || teamComment.getParent().getId() == null){
            this.parentId = 0L;
        }else{
            this.parentId = teamComment.getParent().getId();
        }
        this.id = teamComment.getId();
        this.content = teamComment.getContent();
        this.teamUserid = teamComment.getUser().getId();
        this.teamUserNickName = teamComment.getUser().getNickname();
        this.createDate = teamComment.getCreatedTime();
        this.updateDate = teamComment.getModifiedTime();
        this.commentLikeCount = teamComment.getCommentLikeCount();
    }

}
