package com.withme.api.config.auth;

import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * /login 요청에서 수행되는 service
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername invoked.");
        User userEntity = userRepository.findByUsername(username);

        log.info("{}", userEntity);
        return new PrincipalDetails(userEntity);
    }
}
