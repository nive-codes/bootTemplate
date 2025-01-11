package com.nive.prjt.exception.business;

import com.nive.prjt.exception.globals.GlobalRestExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
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


    // 최종 에러 처리
    @ExceptionHandler(BusinessRestException.class)
    public ResponseEntity<Object> handleException(BusinessRestException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("message", ex.getMessage());
        response.put("errorCode", ex.getErrorCode());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(new ErrorResponse(ex.getErrorCode(),ex.getMessage()),HttpStatus.BAD_REQUEST);
    }



    // 공통 에러 응답을 위한 클래스
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private String errorCode;
        private String errorMessage;


    }
}
