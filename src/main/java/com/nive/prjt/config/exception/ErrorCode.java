package com.nive.prjt.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author nive
 * @class ErrorCode
 * @desc HttpStatus, error Code, message return 관련 Enum입니다.
 * @since 2025-01-14
 */
@Getter
public enum ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "입력값 검증 실패했습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "요청 메시지 포맷이 올바르지 않습니다."),
    NULL_POINTER(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Null 값이 처리되었습니다."),
    INDEX_OUT_OF_BOUNDS(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "배열 또는 리스트의 인덱스 범위를 벗어났습니다."),
    ARITHMETIC_ERROR(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "연산 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "알 수 없는 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
