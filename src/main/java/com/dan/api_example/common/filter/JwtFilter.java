package com.dan.api_example.common.filter;


import com.dan.api_example.common.exception.BaseException;
import com.dan.api_example.common.response.BaseResponse;
import com.dan.api_example.common.response.BaseResponseStatus;
import com.dan.api_example.entity.User;
import com.dan.api_example.repository.UserRepository;
import com.dan.api_example.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        List<String> uriList = Arrays.asList(
                "/user/login",
                "/user/signup");

        // 1. Token이 필요 없는 경우.
        // 2. Http Method 가 OPTIONS인 경우.
        // - 1, 2와 같은 경우는 JWT토큰 확인하지 않음.
        // OPTIONS ? : 브라우저가 서버에게 지원하는 옵션들을 미리 요청 하는 목적 (preflight)
        if (uriList.contains(request.getRequestURI())
                || request.getMethod().equalsIgnoreCase("OPTIONS")) {

            if (uriList.contains(request.getRequestURI())) {
                log.info("NO NEED TOKEN URI : {}", request.getRequestURI());
            }

            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtUtils.getJwt();

            Authentication authentication = jwtUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (BaseException e) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            BaseResponse<Object> baseResponse = new BaseResponse<>(e.getStatus());
            String jsonRes = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(baseResponse); // BaseResponse 를 JSON object 로 직접 변환 시킴.
            printWriter.print(jsonRes);
            printWriter.flush();
            printWriter.close();
        }
    }

}