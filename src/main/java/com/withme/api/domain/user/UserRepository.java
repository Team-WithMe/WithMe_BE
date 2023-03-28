package com.withme.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByNickname(String nickname);

    /** 일반 로그인 시 이메일 검색 */
    public Optional<User> findByEmailAndPasswordIsNotNull(String email);

    /** OAuth2 로그인 시 계정 검색 */
    public Optional<User> findByJoinRootAndNameAttributeValue(String registrationId, String nameAttributeValue);

    /** OAuth2 로그인 후 토큰에 필요한 userId 조회. 로그인이 완료된 상태이므로 Optional없이 바로 User로 Return */
    public User findByNameAttributeValue(String nameAttributeValue);
}
