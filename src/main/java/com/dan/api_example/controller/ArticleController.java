package com.dan.api_example.controller;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.common.response.BaseResponse;
import com.dan.api_example.model.article.GetArticleRes;
import com.dan.api_example.model.article.PostArticleReq;
import com.dan.api_example.service.ArticleService;
import com.dan.api_example.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService articleService;

    private final JwtUtils jwtUtils;

    /**
     * 게시글 관련 API 시작
     * */

    /**
     * 게시글 생성
     */
    @PostMapping("/board/add")
    public BaseResponse<String> createBoard(@RequestBody PostArticleReq postBoardReq) {
        Long userIdx = jwtUtils.getUserIdV2();

        try {
            articleService.saveArticle(userIdx, postBoardReq);
            return new BaseResponse<>("게시물을 등록하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/board")
    public BaseResponse<String> updateBoard(@RequestBody PostArticleReq postArticleReq, @RequestParam("boardId") Long boardIdx) {
        Long userIdx = jwtUtils.getUserIdV2();
        try {
            articleService.updateArticle(userIdx, boardIdx, postArticleReq);
            return new BaseResponse<>("게시물을 수정하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/board")
    public BaseResponse<String> deleteBoard(@RequestParam("boardId") Long boardIdx) {
        Long userIdx = jwtUtils.getUserIdV2();
        try {
            articleService.deleteArticle(userIdx, boardIdx);
            return new BaseResponse<>("게시물을 삭제하였습니다");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시글 전체 조회
     */
    @GetMapping("/")
    public BaseResponse<List<GetArticleRes>> viewBoards(Pageable pageable) {
        jwtUtils.getUserIdV2();
        try {
            List<GetArticleRes> boardsResList = articleService.viewArticles(pageable);
            return new BaseResponse<>(boardsResList);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 게시글 단일 조회
     */
    @GetMapping("/board")
    public BaseResponse<GetArticleRes> viewSingleBoard(@RequestParam("boardId") Long boardIdx) {
        jwtUtils.getUserIdV2();

        try {
            GetArticleRes articleRes = articleService.viewSingleArticle(boardIdx);
            return new BaseResponse<>(articleRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
