package com.withme.api.domain.skill;

import com.withme.api.domain.teamSkill.TeamSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Skill {

   @Id
   @Column
   @Enumerated(EnumType.STRING)
   private SkillName skillName;

   @OneToMany(mappedBy = "skill")
   private List<TeamSkill> skillTeams = new ArrayList<>();

   @Builder
   public Skill(SkillName skillName) {
      this.skillName = skillName;
   }
}