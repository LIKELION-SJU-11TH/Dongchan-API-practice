package com.dan.api_example.controller;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.common.response.BaseResponse;
import com.dan.api_example.model.article.GetArticleRes;
import com.dan.api_example.model.article.GetArticleResWithImg;
import com.dan.api_example.model.article.PostArticleReq;
import com.dan.api_example.service.ArticleService;
import com.dan.api_example.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
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
            articleService.createArticle(userIdx, postBoardReq);
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

    /**
     * 이미지 업로드 버전
     *
     * @return
     */
    @PostMapping(value = "/board/addV2", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public BaseResponse<String> createBoardV2(@RequestPart PostArticleReq postBoardReq, @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        log.info("MULTIPART DATA : {}", files.get(0));

        Long userIdx = jwtUtils.getUserIdV2();
        if (files == null || files.isEmpty()) {
            try {
                articleService.createArticle(userIdx, postBoardReq);
                return new BaseResponse<>("게시물 업로드에 성공하였습니다.(NO IMG)");
            } catch (BaseException e) {
                return new BaseResponse<>(e.getStatus());
            }
        }

        try {
            articleService.createArticleV2(userIdx, postBoardReq, files);
            return new BaseResponse<>("게시물 업로드에 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/boardV2")
    public BaseResponse<GetArticleResWithImg> viewSingleBoardV2(@RequestParam("boardId") Long boardIdx) {
        jwtUtils.getUserIdV2();

        try {
            GetArticleResWithImg getArticleResWithImg = articleService.viewSingleArticleV2(boardIdx);
            return new BaseResponse<>(getArticleResWithImg);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
