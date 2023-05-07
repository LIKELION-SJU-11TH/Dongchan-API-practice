package com.dan.api_example.controller;

import com.dan.api_example.model.GetUserRes;
import com.dan.api_example.model.SignUpUserReq;
import com.dan.api_example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String createUser(@RequestBody SignUpUserReq signUpUserReq) {
        userService.createUser(signUpUserReq);
        return "ok";
    }

    @GetMapping("/")
    public List<GetUserRes> getUser() {
        List<GetUserRes> getUserRes = userService.getUsers();
        return getUserRes;
    }

    /**
     * 단일 유저 조회
     * 구현해 보세요
     *
     * @RequestParam으로 userId 받아와서 조회
     */
}
