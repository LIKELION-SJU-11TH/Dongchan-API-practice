package com.dan.api_example.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * public enum HttpStatus 와 비슷한 형태
 * 아래 HttpStatus.OK.value()의 HttpStatus 를 ctrl(command) + 클릭 해보세요
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 성공 코드 2xx
     * 코드의 원활한 이해을 위해 code는 숫자가 아닌 아래 형태로 입력해주세요.
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    /**
     * Client Error - 4xx 에러
     */
    NO_AUTH(false, HttpStatus.UNAUTHORIZED.value(), "권한이 없습니다."),
    EXIST_EMAIL(false, HttpStatus.CONFLICT.value(), "이미 존재하는 회원입니다"),
    NON_EXIST_USER(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다"),
    NON_EXIST_ARTICLE(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 게시글입니다."),
    NON_EXIST_IMAGE(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 이미지입니다."),
    NOT_MATCH_PASSWORD(false, HttpStatus.UNAUTHORIZED.value(), "비밀번호가 일치하지 않습니다."),
    NO_SESSION_ID(false, HttpStatus.BAD_REQUEST.value(), "세션아이디가 존재하지 않습니다."),
    NO_JWT(false, HttpStatus.BAD_REQUEST.value(), "JWT 토큰이 존재하지 않습니다."),
    INVALID_TOKEN(false, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(false, HttpStatus.BAD_REQUEST.value(), "만료된 토큰입니다."),
    CONTENT_LENGTH_ERROR(false, HttpStatus.BAD_REQUEST.value(), "내용은 0자 이상 500자 이하까지 입력할 수 있습니다."),
    EMPTY_MULTIPARTFILE(false, HttpStatus.NO_CONTENT.value(), "이미지가 비어있습니다."),

    /**
     * Server Error - 5xx 에러
     */
    DATABASE_INSERT_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 저장에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    DATABASE_DELETE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 삭제에 실패하였습니다."),
    EXTERNAL_API_CONNECTION_ERROR(false, HttpStatus.GATEWAY_TIMEOUT.value(), "외부 API와의 연결이 끊어졌습니다."),
    REDIRECT_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "리다이렉트에 실패하였습니다.");

    /**
     * isSuccess : 요청의 성공 또는 실패
     * code : Http Status Code
     * message : 설명
     */
    private final boolean isSuccess;
    private final int code;
    private final String message;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
