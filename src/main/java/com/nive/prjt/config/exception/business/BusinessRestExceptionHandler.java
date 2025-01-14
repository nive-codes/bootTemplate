package com.nive.prjt.config.exception.business;

import com.nive.prjt.config.response.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nive
 * @class globalExceptionHandler
 * @desc 비즈니스 공통 예외 처리 handler - (REST API)
 * @since 2025-01-11
 */
@RestControllerAdvice
@Slf4j
public class BusinessRestExceptionHandler {

    @ExceptionHandler(BusinessRestException.class)
    public ResponseEntity<Object> handleException(BusinessRestException ex) {
        log.error("Business exception : ErrorCode: {}, Message: {}", ex.getErrorCode(), ex.getMessage());

        // 예외 객체에서 errorCode와 message를 받아서 ErrorResponse 생성
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());

        // 에러 응답을 JSON 형식으로 반환
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
}
