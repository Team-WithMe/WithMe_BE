package com.withme.api.domain.teamComment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @JsonManagedReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private TeamComment parent;

    @JsonBackReference
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<TeamComment> children = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public TeamComment(String content, User user, Team team) {
        this.content = content;
        this.user = user;
        this.team = team;
    }
}
