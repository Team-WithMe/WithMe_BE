package com.withme.api.service;

import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import com.withme.api.exception.UserAlreadyExistException;
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
    public User createUser(JoinRequestDto dto){

        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email Duplicated", "email");
        }
        if (userRepository.findByNickname(dto.getNickname()) != null) {
            throw new UserAlreadyExistException("Nickname Duplicated", "nickname");
        }

        dto.encodePassword(passwordEncoder);

        return userRepository.save(dto.toEntity());
    }

}