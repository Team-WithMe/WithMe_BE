package com.withme.api.domain.skill;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "SKILL")
@Entity
public class Skill {

   @Id
   @Column(length = 50)
   private String skillName;

   @Builder
   public Skill(String skillName) {
      this.skillName = skillName;
   }
}