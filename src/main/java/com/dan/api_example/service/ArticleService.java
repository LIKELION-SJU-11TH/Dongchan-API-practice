package com.dan.api_example.service;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.entity.Article;
import com.dan.api_example.entity.User;
import com.dan.api_example.model.article.GetArticleRes;
import com.dan.api_example.model.article.PostArticleReq;
import com.dan.api_example.model.user.GetUserRes;
import com.dan.api_example.repository.ArticleRepository;
import com.dan.api_example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dan.api_example.common.entity.BaseEntity.State.ACTIVE;
import static com.dan.api_example.common.response.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    public void saveArticle(Long userIdx, PostArticleReq postArticleReq) {
        User user = userRepository.findByIdAndState(userIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
        log.info("USER 이름 : {}", user.getName());

        if (postArticleReq.getContent().length() == 0 || postArticleReq.getContent().length() > 500) {
            throw new BaseException(CONTENT_LENGTH_ERROR);
        }

        Article article = Article.builder()
                .title(postArticleReq.getTitle())
                .content(postArticleReq.getContent())
                .user(user)
                .build();
        log.info("게시글 빌드 성공 : {}", article.getTitle());

        try {
            articleRepository.save(article);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }
    }

    public void updateArticle(Long userId, Long boardId, PostArticleReq postArticleReq) {
        User user = userRepository.findByIdAndState(userId, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));
        log.info("USER 이름 : {}", user.getName());

        Article article = articleRepository.findByIdAndState(boardId, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_ARTICLE));

        if (user != article.getUser()) {
            throw new BaseException(NO_AUTH);
        }

        if (postArticleReq.getContent().length() == 0 || postArticleReq.getContent().length() > 500) {
            throw new BaseException(CONTENT_LENGTH_ERROR);
        }

        article.updateArticle(postArticleReq.getTitle(), postArticleReq.getContent());
        articleRepository.flush();
    }

    public void deleteArticle(Long userIdx, Long boardIdx) {
        User user = userRepository.findByIdAndState(userIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_USER));

        Article article = articleRepository.findByIdAndState(boardIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_ARTICLE));

        if (user != article.getUser()) {
            throw new BaseException(NO_AUTH);
        }

        articleRepository.delete(article);
        articleRepository.flush();
    }

    // 게시물 전체 조회
    public List<GetArticleRes> viewArticles (Pageable pageable) {
        Page<Article> articles = articleRepository.findAll(pageable);

        List<GetArticleRes> getBoardResList = articles.getContent().stream().map(article -> {
            GetArticleRes getArticleRes = new GetArticleRes();
            getArticleRes.setTitle(article.getTitle());
            getArticleRes.setContent(article.getContent());
            getArticleRes.setWriter(new GetUserRes(article.getUser()));
            getArticleRes.setCreatedAt(article.getCreatedAt());
            return getArticleRes;
        }).collect(Collectors.toList());

        return getBoardResList;
    }

    public GetArticleRes viewSingleArticle(Long boardId) {
        Article article = articleRepository.findByIdAndState(boardId, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_ARTICLE));

        GetArticleRes getArticleRes = GetArticleRes.builder()
                .writer(new GetUserRes(article.getUser()))
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .build();

        return getArticleRes;
    }
}
