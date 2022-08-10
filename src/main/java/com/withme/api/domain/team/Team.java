package com.withme.api.domain.team;

import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamUser.TeamUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Team extends BaseTimeEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "team_idx")
   private Long id;

   @Column(nullable = false)
   private String teamName;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private TeamCategory teamCategory;

   @Column(nullable = false)
   private String teamDesc;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private Status status;

   @OneToMany(mappedBy = "team")
   private List<TeamSkill> teamSkills = new ArrayList<>();

   @OneToMany(mappedBy = "team")
   private List<TeamUser> teamUsers = new ArrayList<>();

   @Builder
   public Team(Long id, String teamName, TeamCategory teamCategory, String teamDesc, Status status, List<TeamSkill> teamSkills, List<TeamUser> teamUsers) {
      this.id = id;
      this.teamName = teamName;
      this.teamCategory = teamCategory;
      this.teamDesc = teamDesc;
      this.status = status;
      this.teamSkills = teamSkills;
      this.teamUsers = teamUsers;
   }
}