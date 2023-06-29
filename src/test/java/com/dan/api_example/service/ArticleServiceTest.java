package com.dan.api_example.service;

import com.dan.api_example.entity.Article;
import com.dan.api_example.entity.User;
import com.dan.api_example.model.article.PostArticleReq;
import com.dan.api_example.model.user.SignUpUserReq;
import com.dan.api_example.repository.ArticleRepository;
import com.dan.api_example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleService sut;

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    @DisplayName("게시물 생성(등록)")
    @Test
    public void givenArticleInfo_whenCreatingArticle_thenSaveArticle() {
        // given

        // when

        // then

    }

    @DisplayName("게시물 수정")
    @Test
    public void givenUpdatedArticleInfo_whenUpdating_thenSaveUpdatedArticle() {
        // given

        // when

        // then

    }

    @DisplayName("게시물 삭제")
    @Test
    public void givenArticleId_whenDeletingArticle_thenDeleteArticle() {
        // given

        // when

        // then
    }

    @DisplayName("단일 게시물 조회")
    @Test
    public void givenArticleId_whenReadingArticle_thenReturnArticle() {
        // given

        // when

        // then
    }

    @DisplayName("게시물 리스트 조회")
    @Test
    public void givenNothing_whenReadingArticles_thenReturnArticles() {

    }
}