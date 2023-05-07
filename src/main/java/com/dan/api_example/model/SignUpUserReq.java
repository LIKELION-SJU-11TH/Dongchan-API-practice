package com.dan.api_example.model;

import lombok.Getter;

@Getter
public class SignUpUserReq {
    private String name;
    private int age;
    private String email;
    private String password;
}
