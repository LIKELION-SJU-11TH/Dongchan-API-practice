package com.dan.api_example.model.article;

import lombok.Getter;

@Getter
public class PostArticleReq {
    private String title;
    private String content;

    public PostArticleReq() {
    }

    public PostArticleReq(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
