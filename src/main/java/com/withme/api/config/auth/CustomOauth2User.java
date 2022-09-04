package com.withme.api.config.auth;

import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauth2User extends DefaultOAuth2User {

    private UserRepository userRepository;
    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOauth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, UserRepository userRepository) {
        super(authorities, attributes, nameAttributeKey);
        this.userRepository = userRepository;
    }

    public User getUser() {
        return userRepository.findByNameAttributeValue(this.getName());
    }
}
