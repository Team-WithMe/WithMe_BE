package com.withme.api.domain.team;

import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.teamSkill.TeamSkill;
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




//
//   @ManyToMany
//   @JoinTable(
//           name = "TEAM_SKILL",
//           joinColumns = {@JoinColumn(name = "teamIdx", referencedColumnName = "teamIdx")},
//           inverseJoinColumns = {@JoinColumn(name = "skillName", referencedColumnName = "skillName")}
//   )
//   private Set<Skill> skills = new HashSet<>();
//
//   @ManyToMany
//   @JoinTable(
//           name = "TEAM_USER",
//           joinColumns = {@JoinColumn(name = "teamIdx", referencedColumnName = "teamIdx")},
//           inverseJoinColumns = {@JoinColumn(name = "userIdx", referencedColumnName = "userIdx")}
//   )
//   private List<User> members = new ArrayList<>();


}