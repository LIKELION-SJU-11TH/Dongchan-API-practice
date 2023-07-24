package com.dan.api_example.service;

import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.util.ExternalApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.dan.api_example.common.response.BaseResponseStatus.EXTERNAL_API_CONNECTION_ERROR;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExternalApiService {

    private final ExternalApiUtil externalApiUtil;

    public void getBikeInfo(int startIdx, int amount) throws BaseException {
        log.info("API SERVICE INTRO");
        try {
            externalApiUtil.getRentBikeList(startIdx, amount);
        } catch (Exception e) {
            throw new BaseException(EXTERNAL_API_CONNECTION_ERROR);
        }
        log.info("API SERVICE OUTRO");
    }
}
