package com.dan.api_example.model.article;

import com.dan.api_example.model.image.GetImageRes;
import com.dan.api_example.model.user.GetUserRes;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetArticleResWithImg {
    private GetUserRes writer;
    private String title;
    private String content;
    private List<GetImageRes> imageList;
    private LocalDateTime createdAt;

    @Builder
    public GetArticleResWithImg(GetUserRes writer, String title, String content, List<GetImageRes> imageList, LocalDateTime createdAt) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.imageList = imageList;
        this.createdAt = createdAt;
    }
}
