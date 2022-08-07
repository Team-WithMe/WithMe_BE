package com.withme.api.domain.TeamSkill;

import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.team.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TeamSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamSkillIndex;

    @ManyToOne
    @JoinColumn(name = "TEAM_IDX")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "SKILL_NAME")
    private Skill skill;

}
