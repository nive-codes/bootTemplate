package com.nive.prjt.exception.business;

/**
 * @author nive
 * @class BusinessException
 * @desc service layer의 exception을 발생 시키기 위한 공통 Exception 처리
 * @since 2025-01-10
 */
public class BusinessRestException extends RuntimeException {
    private final String errorCode;

    public BusinessRestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}