package com.withme.api.domain.user;

import com.withme.api.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "USER")
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String username;

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

    @Column
    private String role;

//    public List<String> getRoleList() {
//        if(this.roles.length() > 0) {
//            return Arrays.asList(this.roles.split(","));
//        }
//    }

//    @ManyToMany
//    @JoinTable(
//            name = "USER_AUTHORITY",
//            joinColumns = {@JoinColumn(name = "userIdx", referencedColumnName = "userIdx")},
//            inverseJoinColumns = {@JoinColumn(name = "authotiryName", referencedColumnName = "authorityName")})
//    private Set<Authority> authorities;
//
//    @ManyToMany
//    private List<Team> teams;

    @Builder
    public User(String email, String username, String password, String nickname, boolean activated, String userImage, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.activated = activated;
        this.userImage = userImage;
        this.role = role;
    }
}