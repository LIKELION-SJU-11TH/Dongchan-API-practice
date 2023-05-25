package com.dan.api_example.model.user;


import com.dan.api_example.entity.User;
import lombok.Getter;

@Getter
public class GetUserRes {
    private String email;
    private String name;
    private int age;

    public GetUserRes(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.age = user.getAge();
    }

    public GetUserRes() {
    }
}
