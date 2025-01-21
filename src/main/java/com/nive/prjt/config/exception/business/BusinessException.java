package com.nive.prjt.config.exception.business;

import com.nive.prjt.config.response.ApiCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author nive
 * @class BusinessException
 * @since 2025-01-10
 * @desc service layer의 exception을 발생 시키기 위한 공통 Exception 처리
 *         - MVC 웹 응답, REST API 응답 모두 처리 가능하도록 통합(25.01.21)
 *         - 기본값 설정: HTTP 상태코드, 뷰, 에러 코드 등(25.01.21)
 */
@Getter
public class BusinessException extends RuntimeException {
    private final String message;      // 에러 메시지 (공용)
    private final String returnView;   // 에러 처리 후 리턴할 뷰 (MVC용)  기본적으로 /error/500 활용
    private final String errorCode;    // API 응답에 사용할 에러 코드 (REST API용)
    private final HttpStatus httpStatus;  // HTTP 상태 코드 (REST API용)

    private final ApiCode apiCode;  //ApiCode의 집합체

    /**
     * 1. message만 있는 경우
     * 기본적으로 ApiCode.INTERNAL_SERVER_ERROR를 사용
     * MVC에서 처리되었을 때 반환할 에러 뷰는 기본적으로 "/error/500"을 사용합니다.
     */
    public BusinessException(String message) {
        this(message, "/error/500", ApiCode.INTERNAL_SERVER_ERROR);  // 기본값 설정
    }

    /**
     * 2. ApiCode만 있는 경우
     * ApiCode에서 메시지와 에러 코드, 상태 코드를 모두 가져옴(뷰는 error/500 default 설정)
     * 이 생성자는 주로 REST API에서 사용됩니다.
     */
    public BusinessException(ApiCode apiCode) {
        super(apiCode.getMessage());
        this.message = apiCode.getMessage();
        this.returnView = "/error/500";  // 기본 에러 뷰
        this.errorCode = apiCode.getCode();
        this.httpStatus = apiCode.getStatus();
        this.apiCode = apiCode;
    }

    /**
     * 3. message와 returnView만 있는 경우
     * 기본적으로 ApiCode.INTERNAL_SERVER_ERROR를 사용
     * 이 생성자는 주로 MVC에서 에러 뷰를 반환할 때 사용됩니다.
     * returnView가 "/error/500"을 기본값으로 사용하며, 이 뷰는 MVC에서 화면을 표시할 때 활용됩니다.
     */
    public BusinessException(String message, String returnView) {
        super(message);
        this.message = message;
        this.returnView = returnView != null ? returnView : "/error/500";  // 기본값 설정
        this.errorCode = ApiCode.INTERNAL_SERVER_ERROR.getCode();  // 기본 에러 코드
        this.httpStatus = ApiCode.INTERNAL_SERVER_ERROR.getStatus();  // 기본 상태 코드
        this.apiCode = ApiCode.INTERNAL_SERVER_ERROR;
    }

    /**
     * 4. message와 ApiCode만 있는 경우
     * ApiCode에서 상태 코드와 에러 코드 가져옴
     * 이 생성자는 주로 REST API에서 사용되며, API 응답을 처리하는 데 사용됩니다.(클라이언트 특정 메세지 반환)
     */
    public BusinessException(String message, ApiCode apiCode) {
        this(message, "/error/500", apiCode);  // 기본적으로 view는 "/error/500"을 사용
    }

    /**
     * 5. message와 returnView, ApiCode만 있는 경우
     * ApiCode에서 상태 코드와 에러 코드 가져옴
     * 이 생성자는 MVC와 REST API 양쪽에서 사용될 수 있습니다.
     */
    public BusinessException(String message, String returnView, ApiCode apiCode) {
        super(message);
        this.message = message;
        this.returnView = returnView != null ? returnView : "/error/500";  // 기본값 설정
        this.errorCode = apiCode.getCode();  // ApiCode에서 에러 코드 가져옴
        this.httpStatus = apiCode.getStatus();  // ApiCode에서 상태 코드 가져옴
        this.apiCode = apiCode;
    }

    /**
     * 6. HTTP 상태 코드와 에러 코드, 메시지를 모두 따로 설정할 수 있는 생성자 추가
     * REST API 응답을 처리하기 위한 생성자
     */
    public BusinessException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.returnView = "/error/500";  // 기본값 설정
        this.errorCode = errorCode != null ? errorCode : "INTERNAL_SERVER_ERROR"; // 기본 에러 코드
        this.httpStatus = httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR; // 기본 상태 코드
        this.apiCode = ApiCode.INTERNAL_SERVER_ERROR;  // 기본 ApiCode
    }
}