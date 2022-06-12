package com.withme.api.domain.team;

import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Table(name = "TEAM")
@Entity
public class Team extends BaseTimeEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long teamIdx;

   @Column(nullable = false)
   private String teamName;

   @Column(nullable = false)
   private String teamCategory;

   @Column(nullable = false)
   private String teamDesc;

   @Column(nullable = false)
   private String teamIntroduce;

   @Column(nullable = false)
   private boolean shown;

   @Column(nullable = false)
   private String teamNotice;

   @ManyToMany
   @JoinTable(
           name = "TEAM_SKILL",
           joinColumns = {@JoinColumn(name = "teamIdx", referencedColumnName = "teamIdx")},
           inverseJoinColumns = {@JoinColumn(name = "skillName", referencedColumnName = "skillName")}
   )
   private Set<Skill> skills;

   @ManyToMany
   @JoinTable(
           name = "TEAM_USER",
           joinColumns = {@JoinColumn(name = "teamIdx", referencedColumnName = "teamIdx")},
           inverseJoinColumns = {@JoinColumn(name = "userIdx", referencedColumnName = "userIdx")}
   )
   private List<User> members;

   @Builder
   public Team(String teamName, String teamCategory, String teamDesc, String teamIntroduce, boolean shown, String teamNotice ){
      this.teamName = teamName;
      this.teamCategory = teamCategory;
      this.teamDesc = teamDesc;
      this.teamIntroduce = teamIntroduce;
      this.shown = shown;
      this.teamNotice = teamNotice;
   }

}