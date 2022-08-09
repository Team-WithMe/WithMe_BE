package com.withme.api.domain.teamSkill;

import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TeamSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_skill_index")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_name")
    private Skill skill;

    @Builder
    public TeamSkill(Long id, Team team, Skill skill) {
        this.id = id;
        this.team = team;
        this.skill = skill;
    }
}
