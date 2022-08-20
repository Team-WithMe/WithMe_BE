package com.withme.api.domain.teamSkill;

import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.team.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TEAMSKILL_TEAM_SKILL_UNIQUE", columnNames = {"team_idx", "skill_name"})
})
@Entity
public class TeamSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_skill_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_name")
    private Skill skill;

}
