package com.withme.api.controller.dto;

import com.withme.api.domain.teamNotice.TeamNotice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "팀 공지사항 응답 DTO 객체")
@Getter
@ToString
@NoArgsConstructor
public class TeamNoticeResponseDto {

    @Schema(description = "팀 id", example = "1")
    private Long id;

    @Schema(description = "공지사항 제목", example = "모임 시간 공지")
    private String title;

    @Schema(description = "공지사항 내용", example = "매주 주말 오후 12시 카페")
    private String content;

    @Schema(description = "공지사항 작성일", example = "별다방 스터디 모임")
    private LocalDateTime createdTime;

    public TeamNoticeResponseDto(TeamNotice teamNotice) {
        this.id = teamNotice.getId();
        this.title = teamNotice.getTitle();
        this.content = teamNotice.getContent();
        this.createdTime = teamNotice.getCreatedTime();
    }
}
