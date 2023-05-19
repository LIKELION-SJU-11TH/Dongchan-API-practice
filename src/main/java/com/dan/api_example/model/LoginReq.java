package com.dan.api_example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginReq {
    @Email
    private String email;
    private String password;
}
