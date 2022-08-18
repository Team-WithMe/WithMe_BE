package com.withme.api.domain.skill;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.withme.api.domain.teamSkill.TeamSkill;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Skill {

   @Id
   @Column
   @Enumerated(EnumType.STRING)
   private SkillName skillName;
//   @JsonManagedReference
   @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<TeamSkill> skillTeams = new ArrayList<>();

   @Builder
   public Skill(SkillName skillName) {
      this.skillName = skillName;
   }
}