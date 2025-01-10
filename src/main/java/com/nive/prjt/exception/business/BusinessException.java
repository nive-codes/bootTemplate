package com.nive.prjt.exception.business;

/**
 * @author nive
 * @class BusinessException
 * @desc service layer의 exception을 발생 시키기 위한 공통 Exception 처리
 * @since 2025-01-10
 */
public class BusinessException extends RuntimeException {
    private String message; /*return messages*/
    private String returnView;  /*에러 처리후 return view*/

    public BusinessException(String message, String returnView) {
        super(message);
        this.message = message;
        this.returnView = returnView;
    }

    public String getMessage() {
        return message;
    }

    public String getReturnView() {
        return returnView;
    }
}