package com.withme.api.domain.teamSkill;

import com.fasterxml.jackson.annotation.*;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TEAMSKILL_TEAM_SKILL_UNIQUE", columnNames = {"team_idx", "skill_name"})
})
@Entity
public class TeamSkill {
    @JsonIgnore // NOTE 관계 테이블 ID는 화면에서 필요없기 때문에 제외
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_skill_idx")
    private Long id;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;
    //@JsonBackReference
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER) // NOTE FetchType.EAGER 로 Skill안의 모든 정보를 바로 조회해서 무한루프 안걸리도록함
    @JoinColumn(name = "skill_name")
    private Skill skill;

    @Builder
    public TeamSkill(Team team, Skill skill) {
        this.team = team;
        this.skill = skill;
    }
}
