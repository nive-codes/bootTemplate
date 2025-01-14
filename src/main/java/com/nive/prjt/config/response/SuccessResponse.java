package com.nive.prjt.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hosikchoi
 * @class SuccessResponse
 * @desc 성공시 return response 처리
 * @since 2025-01-14
 */
@Getter
@AllArgsConstructor
public class SuccessResponse<T> {
    private String status;  // 상태 코드 (예: "200 OK")
    private String message;     // 성공 메시지
    private T data;             // 성공적인 데이터 (map)이 default이지만 향후 return 값을 생각해서 T(제너릭)

    public SuccessResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}