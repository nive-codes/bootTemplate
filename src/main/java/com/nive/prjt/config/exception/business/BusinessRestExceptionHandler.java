package com.nive.prjt.config.exception.business;

import com.nive.prjt.config.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author nive
 * @class globalExceptionHandler
 * @desc 비즈니스 공통 예외 처리 handler - (REST API)
 * @since 2025-01-11
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(annotations = RestController.class)
@Slf4j
public class BusinessRestExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleException(BusinessException ex) {
        log.error("BusinessRestExceptionHandler.BusinessException 발생 : {} (View: {}, ErrorCode: {}, HttpStatus: {})",
                ex.getMessage(), ex.getReturnView(), ex.getErrorCode(), ex.getHttpStatus(), ex);

        // ApiResponse의 실패 형식으로 반환 수정
        // ApiCode는 미리 정의되어 있는 것은 수정이 안되므로 각 Exception 처리할 모듈 단에서 처리
        // 일관된 응답을 위한 ApiReponse 처리
        return ApiResponse.fail(
                ex.getErrorCode(), // 커스텀 에러 코드
                ex.getMessage(),   // 커스텀 메시지
                null               // 실패 응답의 데이터는 null로 설정
        );
    }
}
