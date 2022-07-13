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
}
