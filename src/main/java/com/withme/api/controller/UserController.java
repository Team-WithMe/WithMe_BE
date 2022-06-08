package com.withme.api.controller;

import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public String join(@RequestBody JoinRequestDto dto) {
        log.info("join{} invoked", dto);
        userService.join(dto);

        return "join";
    }
}
