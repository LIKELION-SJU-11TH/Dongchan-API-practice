package com.dan.api_example.controller;

import com.dan.api_example.common.filter.JwtFilter;
import com.dan.api_example.common.response.BaseResponse;
import com.dan.api_example.service.ExternalApiService;
import com.dan.api_example.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ExternalApiController {

    private final ExternalApiService externalApiService;
    private final JwtUtils jwtUtils;

    @GetMapping("/bike/{startIdx}/{amount}")
    public BaseResponse<String> getBikeInfo(@PathVariable int startIdx, @PathVariable int amount) {
        jwtUtils.getUserIdV2();
        try {
            log.info("API CONTROLLER START");
            externalApiService.getBikeInfo(startIdx, amount);
            return new BaseResponse<>("OK");
        } catch (Exception e) {
            log.info("API ACCESS FAILED _ CONTROLLER");
            return new BaseResponse<>("ERROR");
        }
    }
}
