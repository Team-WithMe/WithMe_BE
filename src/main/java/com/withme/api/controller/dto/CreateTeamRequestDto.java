package com.withme.api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
@Schema(description = "팀 생성 요청 DTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class CreateTeamRequestDto {

    @Schema(description = "팀 목적", example = "취미로 개발하실분을 모집합니다.", required = true)
    @NotNull
    @Size(min = 1, max = 100, message = "팀 목적은 100글자 이하입니다.")
    private String goal;
    @Schema(description = "팀 스킬", allowableValues = {"nodejs", "java"}, required = true)
    @NotNull
    private List<String> skills;
    @Schema(description = "팀 이름", example = "일반팀", required = true)
    @NotNull
    @Size(min = 1, max = 20, message = "팀 이름은 20글자 이하입니다.")
    private String name;
    @Schema(description = "팀 설명", example = "취미로 코딩하실분들을 위한 팀입니다.", required = true)
    @NotNull
    @Size(min = 1, max = 200, message = "팀 설명은 200글자 이하입니다.")
    private String description;


    public CreateTeamRequestDto(String goal, List<String> skills, String name, String description) {
        this.goal = goal;
        this.skills = skills;
        this.name = name;
        this.description = description;
    }
}
