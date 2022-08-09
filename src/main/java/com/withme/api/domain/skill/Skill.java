package com.withme.api.domain.skill;

import com.withme.api.domain.teamSkill.TeamSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Skill {

   @Id
   @Column(length = 50, insertable = false, updatable = false)
   private SkillName skillName;

   @OneToMany(mappedBy = "skill")
   private List<TeamSkill> teamSkills = new ArrayList<>();

   @Builder
   public Skill(SkillName skillName) {
      this.skillName = skillName;
   }
}