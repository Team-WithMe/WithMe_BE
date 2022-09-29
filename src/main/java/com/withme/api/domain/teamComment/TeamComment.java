package com.withme.api.domain.teamComment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.commentLike.CommentLike;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class TeamComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Formula("(select count(1) from comment_like cl where cl.comment_id = comment_id)")
    @ColumnDefault("0")
    @Column(name = "comment_like_count", length = 1000)
    private Integer commentLikeCount;

    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private TeamComment parent;

    @JsonManagedReference
    @OneToMany(mappedBy = "parent")// , orphanRemoval = true
    private List<TeamComment> children = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "teamComment", cascade = CascadeType.ALL)
    private List<CommentLike> commentLike = new ArrayList<>();

    public TeamComment(String content, User user, Team team) {
        this.content = content;
        this.user = user;
        this.team = team;
    }

    public void setTeamCommentByContent(String content){
        this.content = content;
    }
}
