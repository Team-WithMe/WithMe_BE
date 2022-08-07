package com.withme.api.config.auth;

import com.withme.api.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@ToString
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickname;
    private String email;
    private String userImage;
    private String nameAttributeValue;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String nameAttributeValue){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = name;
        this.email = email;
        this.userImage = picture;
        this.nameAttributeValue = nameAttributeValue;
    }

    //OAuth2User에서 반환하는 사용자 정보는 Map이기 때문에 값 하나하나를 변환해야 한다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        log.debug("attirbutes : {}", attributes);
        log.debug("registrationId : " + registrationId);

        OAuthAttributes oAuthAttributes;
        switch(registrationId){
            case "naver":
                oAuthAttributes = ofNaver("id", attributes);
                break;
            case "google":
                oAuthAttributes = ofGoogle(userNameAttributeName, attributes);
                break;
            default:
                oAuthAttributes = ofGitHub(userNameAttributeName, attributes);
                break;
        }

        return oAuthAttributes;

    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        log.debug("OAuthAttributes_ofGoogle");
        return OAuthAttributes.builder()
                .name(attributes.get("name") + "_" + attributes.get(userNameAttributeName))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nameAttributeValue(attributes.get(userNameAttributeName).toString())
                .build();
    }

    private static OAuthAttributes ofGitHub(String userNameAttributeName, Map<String, Object> attributes){
        log.debug("OAuthAttributes_ofGitHub");
        return OAuthAttributes.builder()
                .name(attributes.get("name") + "_" + attributes.get(userNameAttributeName))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nameAttributeValue(attributes.get(userNameAttributeName).toString())
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        log.debug("OAuthAttributes_ofNaver");
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return OAuthAttributes.builder()
                .name(response.get("name") + "_" + response.get(userNameAttributeName))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .nameAttributeValue(response.get(userNameAttributeName).toString())
                .build();
    }

    //OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입 시
    public User toEntity(String registrationId, String nameAttributeValue){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .userImage(userImage)
                .role("ROLE_USER")
                .joinRoot(registrationId)
                .nameAttributeValue(nameAttributeValue)
                .build();
    }

}
