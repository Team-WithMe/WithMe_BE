package com.withme.api.config.auth;

import com.withme.api.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@ToString
@Getter
@RequiredArgsConstructor
public class PrincipalDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(() -> user.getRole());
        return authorities;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Long getUserIdx() {
        return user.getUserIdx();
    }

    public boolean isActivated() {
        return user.isActivated();
    }

    public String getUserImage(){
        return user.getUserImage();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();  //Username을 사용하지 않으므로 Nickname 리턴.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
