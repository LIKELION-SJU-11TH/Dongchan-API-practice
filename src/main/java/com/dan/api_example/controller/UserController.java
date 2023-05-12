package com.dan.api_example.controller;

import com.dan.api_example.model.GetUserRes;
import com.dan.api_example.model.SignUpUserReq;
import com.dan.api_example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 기능
     *
     ** BindingResult : 검증오류가 발생할 시 오류 내용을 보관하는 객체.
     *
     * @param signUpUserReq (name, age, email, password)
     * @param result ()
     * @return
     */
    @PostMapping("/signup")
    public String createUser(@RequestBody @Valid SignUpUserReq signUpUserReq, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldError().getDefaultMessage();// 에러의 메시지를 빼올 수 있음. (getFieldError() : 해당 필드의 에러 내용을 모두 빼옴. getDefaultMessage() : 에러 중 메시지를 빼옴.)
            return errorMessage;
        }

        try {
            userService.createUser(signUpUserReq);
        } catch (Exception e) { //만약 UserService의 createUser()에서 에러가 말생하면 catch 구문 실행
            return e.getMessage(); //에러 메시지를 반환.
        }
        return "ok";
    }

    /**
     * 회원 전부 조회하기 기능
     * @return
     */
    @GetMapping("/")
    public List<GetUserRes> getUser() {
        List<GetUserRes> getUserRes = userService.getUsers();
        return getUserRes;
    }

    /**
     * 단일 유저 조회
     * case 1. UserService의 getUserById 메서드를 통해 GetUserRes 받아와서 반환.
     * case 2. 만약 UserService의 getUserById에서 예러 발생 시 null값 반환.
     * @RequestParam으로 userId 받아와서 조회
     */
    @GetMapping(value = "/", params = "userId")
    public GetUserRes getUserById(@RequestParam Long userId) {
        try {
            GetUserRes userRes = userService.getUserById(userId);
            return userRes;
        } catch (Exception e) {
            log.info("Error Message = {}", e.getMessage()); // 지금 단계에서는 에러 내용을 콘솔 창에 띄우기만. todo: 에러 시 Client 에게 따로 보낼 Response를 만들 계획.
            return null;
        }
    }
}
