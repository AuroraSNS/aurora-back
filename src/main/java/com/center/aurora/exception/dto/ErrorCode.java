package com.center.aurora.exception.dto;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "S_001", "서버에 문제가 생겼습니다."),

    NON_EXISTENT_USER(404, "US_001", "존재하지 않는 사용자입니다."),

    BAD_LOGIN_EMAIL(400, "AU_001", "잘못된 아이디입니다."),
    BAD_LOGIN_PASSWORD(400, "AU_002", "잘못된 패스워드입니다."),
    BAD_LOGIN_PROVIDER(400, "AU_002", "로그인 방식이 올바르지 않습니다."),
    DUPLICATED_EMAIL(400, "AU_003", "이미 존재하는 이메일입니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}