package com.withme.api.domain.teamNotice;

import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class TeamNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_notice_idx")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_idx")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User writer;

    @Builder
    public TeamNotice(String title, String content, Team team, User writer) {
        this.title = title;
        this.content = content;
        this.team = team;
        this.writer = writer;

        //양방향 연관관계를 위한 로직
        team.getTeamNotice().add(this);
        writer.getTeamNotice().add(this);
    }

}
