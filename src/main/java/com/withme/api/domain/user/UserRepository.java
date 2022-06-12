package com.withme.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

    public User findByEmail(String email);

    public User findByNickname(String nickname);
}
