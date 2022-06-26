package com.withme.api.domain.team;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class TeamVo {

    // NOTE 팀 목적
    private String goal;
    // NOTE 팀 사용기술
    private List<String> skills;
    // NOTE 팀 이름
    private String name;
    // NOTE 팀 설명
    private String description;


    public TeamVo(String goal, List<String> skills, String name, String description) {
        this.goal = goal;
        this.skills = skills;
        this.name = name;
        this.description = description;
    }
}
