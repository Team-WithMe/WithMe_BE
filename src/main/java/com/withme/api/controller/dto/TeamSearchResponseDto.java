package com.withme.api.controller.dto;

import com.withme.api.domain.skill.Skill;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Schema(description = "팀 조회 리스폰 DTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
public class TeamSearchResponseDto {

    @Schema(description = "팀 스킬", allowableValues = {"nodejs", "java"}, required = true)
    @NotNull
    private Set<Skill> skills;

    public TeamSearchResponseDto(Set<Skill> skills) {
        this.skills = skills;
    }
}
