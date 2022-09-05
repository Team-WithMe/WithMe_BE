package com.withme.api.domain.teamUser;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "TEAMUSER_TEAM_USER_UNIQUE", columnNames = {"team_idx", "user_idx"})
})
@Entity
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_user_idx")
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @Builder
    public TeamUser(MemberType memberType, Team team, User user) {
        this.memberType = memberType;
        this.team = team;
        this.user = user;
    }

}
