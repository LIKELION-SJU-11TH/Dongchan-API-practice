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
    private Long boardId;
    private GetUserRes writer;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public GetArticleRes(Long boardId, GetUserRes writer, String title, String content, LocalDateTime createdAt) {
        this.boardId = boardId;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
