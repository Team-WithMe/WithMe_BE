package com.withme.api.domain.user;

import com.withme.api.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "USER")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean activated;

    @Column
    private String userImage;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String joinRoot;

//    @ManyToMany
//    private List<Team> teams;

    @Builder
    public User(String email, String password, String nickname, boolean activated, String userImage, String role, String joinRoot) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.activated = activated;
        this.userImage = userImage;
        this.role = role;
        this.joinRoot = joinRoot;
    }

    public User update(String userImage) {
        this.userImage = userImage;

        return this;
    }
}