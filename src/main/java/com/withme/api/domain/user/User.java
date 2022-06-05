package com.withme.api.domain.user;

import com.withme.api.domain.BaseTimeEntity;
import com.withme.api.domain.authority.Authority;
import com.withme.api.domain.team.Team;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@Table(name = "USER")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean activated;

    @Column
    private String userImage;

    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "userIdx", referencedColumnName = "userIdx")},
            inverseJoinColumns = {@JoinColumn(name = "authotiryName", referencedColumnName = "authorityName")})
    private Set<Authority> authorities;

    @ManyToMany
    private List<Team> teams;

    @Builder
    public User(String email, String password, String nickname, boolean activated, String userImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.activated = activated;
        this.userImage = userImage;
    }
}