package com.withme.api.controller.dto;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.teamNotice.TeamNotice;
import com.withme.api.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(description = "팀 공지사항 등록 요청 DTO 객체")
@Getter
@ToString
@NoArgsConstructor
public class TeamNoticeCreateRequestDto {

    @Schema(description = "공지사항 제목", example = "모임시간 공지", required = true)
    @NotBlank
    @Size(min = 1, max = 50, message = "제목은 50자 이내로 입력해주세요.")
    private String title;

    @Schema(description = "공지사항 내용", example = "모임은 매주 일요일 오후 1시에 사거리 카페에서 합니다.", required = true)
    @NotBlank
    @Size(min = 10, max = 1000, message = "내용은 10자 이상 1000자 이하로 입력해주세요.")
    private String content;

    @Builder
    public TeamNoticeCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public TeamNotice toEntity(Team team, User user) {
        return new TeamNotice().builder()
                .title(this.title)
                .content(this.content)
                .team(team)
                .writer(user)
                .build();
    }

}