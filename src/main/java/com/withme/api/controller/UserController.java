package com.withme.api.controller;

import com.withme.api.controller.dto.JoinRequestDto;
import com.withme.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "회원가입"
        , description = "새로운 유저의 정보를 DB에 저장한다."
    )
    @PostMapping("/join")
    public String join(@RequestBody JoinRequestDto dto) {
        log.debug("join{} invoked", dto);

        userService.join(dto);

        // Todo 회원 가입 후 return 어떻게 처리할지 프론트와 협의하기

        return "join completed";
    }
}
