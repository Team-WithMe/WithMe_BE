package com.withme.api.service;

import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.controller.dto.MyPageResponseDto;
import com.withme.api.controller.dto.UserUpdateRequestDto;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import com.withme.api.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(JoinRequestDto dto){
        if(userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BusinessException("Email Duplicated");
        }
        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
            throw new BusinessException("Nickname Duplicated");
        }
        dto.encodePassword(passwordEncoder);

        return userRepository.save(dto.toEntity());
    }

    @Transactional
    public void changeUserNickname(Long userId, UserUpdateRequestDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found. id : " + userId));

        if(userRepository.findByNickname(dto.getNickname()).isPresent()) {
            throw new BusinessException("Nickname Duplicated");
        } else {
            user.changeNickname(dto.getNickname());
        }
    }

    @Transactional
    public MyPageResponseDto getUserAndTeamInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found. id : " + userId));

        return new MyPageResponseDto(user, user.joinedTeamList());
    }
}