package com.withme.api.domain.teamLike;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@DynamicInsert
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TEAMLIKE_TEAM_LIKE_UNIQUE", columnNames = {"team_idx", "user_idx"})
})
@Entity
public class TeamLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_like_idx")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    public TeamLike(Team team, User user) {
        this.team = team;
        this.user = user;
    }
}
