package com.nive.prjt.exception.globals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author nive
 * @class GlobalRestExceptionHandler
 * @desc 공통된 오류코드 처리 + all Exception 처리 포함 (REST API)
 * @since 2025-01-10
 */
@RestControllerAdvice
@Slf4j
public class GlobalRestExceptionHandler {

    // 404 Not Found: 리소스를 찾을 수 없는 경우
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NoSuchElementException ex) {
        log.warn("리소스를 찾을 수 없습니다: {}", ex.getMessage());  // WARN 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
    }

    // 400 Bad Request: 입력값이 잘못된 경우 (예: 유효성 검사 실패)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getDefaultMessage())
                .collect(Collectors.toList());
        log.warn("입력값 검증 실패: {}", errorMessages);  // WARN 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "입력값 검증 실패", errorMessages), HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request: 요청이 잘못된 포맷으로 들어온 경우 (예: JSON 포맷 오류)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.warn("잘못된 메시지 포맷: {}", ex.getMessage());  // WARN 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "요청 메시지 포맷이 올바르지 않습니다."), HttpStatus.BAD_REQUEST);
    }

    // 404 Not Found: 자원 미존재 (예: URL 경로가 틀린 경우)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleResourceNotFoundException(NoHandlerFoundException ex) {
        log.warn("잘못된 경로 요청: {}", ex.getMessage());  // WARN 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", "요청한 자원을 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
    }

    // 400 Bad Request: Null 값이 잘못 사용된 경우
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        log.error("NullPointerException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "Null 값이 처리되었습니다."), HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request: 배열 인덱스 범위 초과
    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
        log.error("IndexOutOfBoundsException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "배열 또는 리스트의 인덱스 범위를 벗어났습니다."), HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request: 숫자 계산 오류 (예: 0으로 나누기)
    @ExceptionHandler(ArithmeticException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException ex) {
        log.error("ArithmeticException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "산술 연산 오류가 발생했습니다."), HttpStatus.BAD_REQUEST);
    }

    // 400 Bad Request: 리소스를 찾을 수 없는 경우 (특정 favicon.ico 관련)
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex) {
        if (ex.getMessage().contains("favicon.ico")) {
            log.warn("favicon.ico 리소스가 존재하지 않습니다. 예외를 무시합니다.");
            return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "favicon.ico 리소스를 찾을 수 없습니다."), HttpStatus.BAD_REQUEST);
        }

        log.error("NoResourceFoundException 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그 출력
        return new ResponseEntity<>(new ErrorResponse("BAD_REQUEST", "리소스를 찾을 수 없습니다."), HttpStatus.BAD_REQUEST);
    }

    // 최종 에러 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("예외 발생: {}", ex.getMessage(), ex);  // ERROR 레벨로 로그
        return new ResponseEntity<>(new ErrorResponse("INTERNAL_SERVER_ERROR", "알 수 없는 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 공통 에러 응답을 위한 클래스
    @Getter
    @Setter
    @AllArgsConstructor
    public static class ErrorResponse {
        private String errorCode;
        private String errorMessage;
        private List<String> details;

        public ErrorResponse(String errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

//        public ErrorResponse(String errorCode, String errorMessage, List<String> details) {
//            this.errorCode = errorCode;
//            this.errorMessage = errorMessage;
//            this.details = details;
//        }
    }
}
