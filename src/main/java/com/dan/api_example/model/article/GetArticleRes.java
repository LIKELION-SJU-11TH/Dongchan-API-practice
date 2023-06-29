package com.dan.api_example.model.article;

import com.dan.api_example.entity.User;
import com.dan.api_example.model.user.GetUserRes;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetArticleRes {
    private GetUserRes writer;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public GetArticleRes(GetUserRes writer, String title, String content, LocalDateTime createdAt) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
