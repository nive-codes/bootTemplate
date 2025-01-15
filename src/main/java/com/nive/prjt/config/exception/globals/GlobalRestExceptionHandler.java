package com.nive.prjt.config.exception.globals;

import com.nive.prjt.config.response.ApiCode;
import com.nive.prjt.config.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author nive
 * @class GlobalRestExceptionHandler
 * @desc 공통된 오류코드 처리 + all Exception 처리 포함 (REST API)
 * ErrorResponse -> ApiResponse 수정 (25.01.15)
 * @since 2025-01-10
 */
@RestControllerAdvice
@Slf4j
@Profile("rest")
public class GlobalRestExceptionHandler {


    // 404 Not Found: 리소스를 찾을 수 없는 경우
    @ExceptionHandler(NoSuchElementException.class)
    public ApiResponse<?> handleNotFoundException(NoSuchElementException ex) {
        log.warn("리소스를 찾을 수 없습니다: {}", ex.getMessage());
        return ApiResponse.fail(ApiCode.NOT_FOUND);
    }

    // 400 Bad Request: 입력값이 잘못된 경우 (유효성 검사 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getDefaultMessage())
                .collect(Collectors.toList());
        log.warn("입력값 검증 실패: {}", errorMessages);
        return ApiResponse.fail(ApiCode.VALIDATION_FAILED, Map.of("errors", errorMessages));
    }

    // 400 Bad Request: 요청이 잘못된 포맷으로 들어온 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("잘못된 메시지 포맷: {}", ex.getMessage());
        return ApiResponse.fail(ApiCode.INVALID_FORMAT);
    }

    // 404 Not Found: 자원 미존재
    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<?> handleResourceNotFoundException(NoHandlerFoundException ex) {
        log.warn("잘못된 경로 요청: {}", ex.getMessage());
        return ApiResponse.fail(ApiCode.NOT_FOUND);
    }

    // 400 Bad Request: Null 값이 잘못 사용된 경우
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<?> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException 발생: {}", ex.getMessage(), ex);
        return ApiResponse.fail(ApiCode.NULL_POINTER);
    }

    // 400 Bad Request: 배열 인덱스 범위 초과
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ApiResponse<?> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        log.error("IndexOutOfBoundsException 발생: {}", ex.getMessage(), ex);
        return ApiResponse.fail(ApiCode.INDEX_OUT_OF_BOUNDS);
    }

    // 400 Bad Request: 숫자 계산 오류
    @ExceptionHandler(ArithmeticException.class)
    public ApiResponse<?> handleArithmeticException(ArithmeticException ex) {
        log.error("ArithmeticException 발생: {}", ex.getMessage(), ex);
        return ApiResponse.fail(ApiCode.ARITHMETIC_ERROR);
    }

    // 400 Bad Request: 리소스를 찾을 수 없는 경우 (특정 favicon.ico 관련)
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<?> handleNoResourceFoundException(NoResourceFoundException ex) {
        if (ex.getMessage().contains("favicon.ico")) {
            log.warn("favicon.ico 리소스가 존재하지 않습니다. 예외를 무시합니다.");
            return ApiResponse.fail(ApiCode.INVALID_FORMAT);
        }
        log.error("NoResourceFoundException 발생: {}", ex.getMessage(), ex);
        return ApiResponse.fail(ApiCode.INVALID_FORMAT);
    }

    // 최종 에러 처리
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception ex) {
        log.error("예외 발생: {}", ex.getMessage(), ex);
        return ApiResponse.fail(ApiCode.INTERNAL_SERVER_ERROR);
    }
}
