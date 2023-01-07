package com.withme.api.domain.commentLike;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.teamComment.TeamComment;
import com.withme.api.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "COMMENTLIKE_COMMENT_LIKE_UNIQUE", columnNames = {"comment_id", "user_id", "team_id"})
})
@Entity
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_idx")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private TeamComment teamComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public CommentLike(Team team, User user, TeamComment teamComment) {
        this.teamComment = teamComment;
        this.user = user;
        this.team = team;
    }
}
