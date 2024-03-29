package com.withme.api.domain.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.commentLike.CommentLike;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.teamLike.TeamLike;
import com.withme.api.domain.teamNotice.TeamNotice;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
                @UniqueConstraint(name = "USER_EMAIL_ROOT_ATTRIBUTE_UNIQUE", columnNames = {"email", "joinRoot", "nameAttributeValue"})
                , @UniqueConstraint(name = "USER_NICKNAME_UNIQUE", columnNames = "nickname")
        })
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String userImage;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String joinRoot;

    @Column
    private String nameAttributeValue;

    @JsonBackReference // NOTE JSON 직렬화 방향 명시
    @OneToMany(mappedBy = "user")
    private List<TeamUser> userTeams = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<TeamNotice> teamNotice = new ArrayList<>();

    // NOTE 팀 추천
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<TeamLike> teamLike = new ArrayList<>();

    // NOTE 댓글 추천
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<CommentLike> commentLike = new ArrayList<>();

    @Builder
    public User(String email, String password, String nickname, String userImage, String role, String joinRoot, String nameAttributeValue) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userImage = userImage;
        this.role = role;
        this.joinRoot = joinRoot;
        this.nameAttributeValue = nameAttributeValue;
    }

    public User update(String userImage) {
        this.userImage = userImage;
        return this;
    }

    public User changeNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void joinTeam(Team team){
        TeamUser teamUser = TeamUser.builder()
                .team(team)
                .user(this)
                .memberType(MemberType.MEMBER)
                .build();
    }

    public List<Team> joinedTeamList() {
        List<Team> teamList = new ArrayList<>();

        this.userTeams.forEach(teamUser -> {
            teamList.add(teamUser.getTeam());
        });

        return teamList;
    }

}