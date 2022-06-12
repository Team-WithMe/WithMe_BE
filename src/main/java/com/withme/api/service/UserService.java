package com.withme.api.service;

import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(JoinRequestDto dto){

        if(userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        dto.encodePassword(passwordEncoder);

        return userRepository.save(dto.toEntity()).getUserIdx();
    }

}
