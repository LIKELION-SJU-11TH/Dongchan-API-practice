package com.dan.api_example.service;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.entity.Article;
import com.dan.api_example.entity.Image;
import com.dan.api_example.entity.User;
import com.dan.api_example.model.article.GetArticleRes;
import com.dan.api_example.model.article.GetArticleResWithImg;
import com.dan.api_example.model.article.PostArticleReq;
import com.dan.api_example.model.image.GetImageRes;
import com.dan.api_example.model.user.GetUserRes;
import com.dan.api_example.repository.ArticleRepository;
import com.dan.api_example.repository.ImageRepository;
import com.dan.api_example.repository.UserRepository;
import com.dan.api_example.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    private final ImageUtil imageUtil;
    private final ImageRepository imageRepository;

    public Article createArticle(Long userIdx, PostArticleReq postArticleReq) {
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
            return articleRepository.save(article);
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

    public void createArticleV2(Long userIdx, PostArticleReq postArticleReq, List<MultipartFile> files) {
        Article savedArticle = createArticle(userIdx, postArticleReq);
        List<String> imgNameList;
        try {
            imgNameList = imageUtil.uploadImage(files);
        } catch (Exception e) {
            log.info("Exception e : {}", e.getMessage());
            throw new BaseException(EMPTY_MULTIPARTFILE);
        }

        try {
            for (String imgName : imgNameList) {
                Image imgFile = Image.builder()
                        .url(imgName)
                        .article(savedArticle).build();
                imageRepository.save(imgFile);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }
    }

    public GetArticleResWithImg viewSingleArticleV2(Long boardId) {
        Article article = articleRepository.findByIdAndState(boardId, ACTIVE)
                .orElseThrow(() -> new BaseException(NON_EXIST_ARTICLE));

        List<GetImageRes> data = new ArrayList<>();
        if (article.getImages() != null) {
            List<Image> files = article.getImages();
            for (Image file : files) {
                Long fileId = file.getId();
                Image image = imageRepository.findById(fileId).orElseThrow(() -> new BaseException(NON_EXIST_IMAGE));

                String stringUrl = image.getUrl();
                Path imagePath = Paths.get(stringUrl);

                byte[] imgData = null;
                try {
                    imgData = Files.readAllBytes(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                data.add(new GetImageRes(imgData));
                log.info("DATA : {}", data.get(0).getData().length);
            }
        }

        GetArticleResWithImg getArticleResWithImg = GetArticleResWithImg.builder()
                .writer(new GetUserRes(article.getUser()))
                .title(article.getTitle())
                .content(article.getContent())
                .createdAt(article.getCreatedAt())
                .imageList(data)
                .build();

        return getArticleResWithImg;
    }
}
