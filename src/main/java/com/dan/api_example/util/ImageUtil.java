package com.dan.api_example.util;

import io.jsonwebtoken.lang.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ImageUtil {

    public List<String> uploadImage(List<MultipartFile> requestImgFiles) throws Exception {
        if (requestImgFiles.isEmpty()) {
            throw new Exception("Empty requestImgFiles");
        }

        String absolutePath = new File("").getAbsolutePath() + "/";
        log.info("absolutePath : {}", absolutePath);

        String path = "imagesResource/";

        File file = new File(path);

        if (!file.exists()) {
            log.info("NO DIR");
            file.mkdirs();
        }

        List<String> fileNameArr = new ArrayList<>();

        for (MultipartFile requestImgFile : requestImgFiles) {
            if (!requestImgFile.isEmpty()) {
                String contentType = requestImgFile.getContentType();
                String fileExtension;
                log.info("contentType : {}", contentType);
                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentType.contains("image/jpeg")) {
                        fileExtension = ".jpg";
                    } else if (contentType.contains("image/png")) {
                        fileExtension = ".png";
                    } else if (contentType.contains("image/gif")) {
                        fileExtension = ".gif";
                    } else break;
                }

                String savedFileName = System.nanoTime() + fileExtension;
                log.info("savedFileName : {}", savedFileName);
                log.info("ppp : {}", absolutePath + path + "/" + savedFileName);
                file = new File(absolutePath + path + savedFileName);
                requestImgFile.transferTo(file);

                fileNameArr.add(absolutePath + path + savedFileName);
            }
        }

        return fileNameArr;
    }

}
