package com.withme.api.domain.team;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.withme.api.domain.BaseTimeCustomEntity;
import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.commentLike.CommentLike;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.teamComment.TeamComment;
import com.withme.api.domain.teamLike.TeamLike;
import com.withme.api.domain.teamNotice.TeamNotice;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@DynamicInsert
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TEAM_TEAMNAME_UNIQUE", columnNames = "teamName")
})
@Entity
public class Team extends BaseTimeCustomEntity {

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

   // NOTE 팀 게시물 제목
   @Column(name = "title", length = 100)
   private String title;

   // NOTE 팀 게시물 내용
   @Column(name = "content", length = 1000)
   private String content;

   // NOTE 팅 조회수
   @ColumnDefault("0")
   @Column(name = "view_count", length = 100000)
   private Integer viewCount;

   // NOTE 팀 댓글 개수
   @Formula("(select count(1) from team_comment tc where tc.team_id = team_idx)")
   @ColumnDefault("0")
   @Column(name = "comment_count", length = 1000)
   private Integer commentCount;

   // NOTE 팀 좋아요 개수
   @Formula("(select count(1) from team_like tl where tl.team_idx = team_idx)")
   @ColumnDefault("0")
   @Column(name = "team_like_count", length = 1000)
   private Integer teamLikeCount;

   @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
   private List<TeamSkill> teamSkills = new ArrayList<>();

   @OneToMany(mappedBy = "team", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)// NOTE cascade = CascadeType.ALL 해야 팀 생성시 팀 유저 정보도 저장됨
   private List<TeamUser> teamUsers = new ArrayList<>();

   @OneToMany(mappedBy = "team")
   private List<TeamNotice> teamNotice = new ArrayList<>();

   @JsonManagedReference
   // NOTE 팀 댓글
   @OneToMany(mappedBy = "team", orphanRemoval = true)
   private List<TeamComment> comments = new ArrayList<>();

   // NOTE 팀 좋아요
   @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
   private List<TeamLike> teamLike = new ArrayList<>();

   // NOTE 댓글 좋아요
   @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
   private List<CommentLike> commentLike = new ArrayList<>();

   @Builder
   public Team(String teamName, TeamCategory teamCategory, String teamDesc, Status status) {
      this.teamName = teamName;
      this.teamCategory = teamCategory;
      this.teamDesc = teamDesc;
      this.status = status;
   }
   /**
    *  팀 스킬 등록
    * */
   public void addTeamSkill(TeamSkill teamSkill){
      teamSkills.add(teamSkill);
   }
   /**
    *  팀 유저 등록
    * */
   public void addTeamUser(TeamUser teamUser){
      teamUsers.add(teamUser);
   }

    public boolean IsUserTeamMember(Long userId) {
       for (TeamUser teamUser : this.teamUsers) {
          if (teamUser.getUser().getId().equals(userId)) { return true; }
       }
       return false;
    }

    public boolean IsUserTeamLeader(Long userId) {
       if (this.IsUserTeamMember(userId)) {
          for (TeamUser teamUser : this.teamUsers) {
             if (teamUser.getUser().getId().equals(userId) &&
                     teamUser.getMemberType().equals(MemberType.LEADER)) {
                return true;
             }
          }
       }
       return false;
    }
    /**
     * 팀 게시물 dto 입력
     * */
    public Long toTeamByTeamPost(String title, String content) {
       this.title = title;
       this.content = content;
       this.status = Status.DISPLAYED;
       this.setModifiedTime(LocalDateTime.now());
       return this.getId();
    }
    /**
     * 조회수 증가
     * */
    public Team addViewCount() {
       this.viewCount += 1;
       return this;
    }
    /**
     * 팀 스킬이름 리스트만 가져오기
     * */
    public List<SkillName> getTeamSkillNameList() {
       return this.getTeamSkills().stream()
               .map(v -> v.getSkill().getSkillName())
               .collect(Collectors.toList());
    }

}