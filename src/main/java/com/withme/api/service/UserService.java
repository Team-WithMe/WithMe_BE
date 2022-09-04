package com.withme.api.service;

import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.controller.dto.MyPageResponseDto;
import com.withme.api.controller.dto.UserUpdateRequestDto;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import com.withme.api.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
            throw new UserAlreadyExistException("Nickname Duplicated", "nickname");
        }

        dto.encodePassword(passwordEncoder);

        return userRepository.save(dto.toEntity());
    }

    @Transactional
    public void changeUserNickname(Long id, UserUpdateRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found. id : " + id));

        if(userRepository.findByNickname(dto.getNickname()).isPresent()) {
            throw new UserAlreadyExistException("Nickname Duplicated", "nickname");
        } else {
            user.changeNickname(dto.getNickname());
        }
    }

    @Transactional
    public MyPageResponseDto getUserAndTeamInfo(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found. id : " + id));

        return new MyPageResponseDto(user, user.joinedTeamList());
    }
}