package com.withme.api.config.auth;


import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException { //Oauth 로그인 완료 후 받은 code를 받고 Access Token을 요청해 userRequest가 access token을 갖고 있음.
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest); //Oauth 제공 서비스 회사로부터 회원 프로필을 받음

        String registrationId = userRequest.getClientRegistration().getRegistrationId();    //현재 로그인 진행 중인 서비스를 구분. ex)google, naver, etc..
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            //OAuth2 로그인 진행 시 키가 되는 필드값. Primary Key
            //구글의 기본 코드는 "sub", 네이버와 카카오는 기본 지원X

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oauth2User.getAttributes());
        //OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스.

        log.debug("OAuth_attributes : {}", attributes);

        User user = saveOrUpdate(attributes, registrationId);

        return new CustomOauth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole())), attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes, String registrationId){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getUserImage()))
                .orElse(attributes.toEntity(registrationId));

        log.debug("user : {}", user);

        return userRepository.save(user);
    }

}
