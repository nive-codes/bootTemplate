package com.nive.prjt.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author nive
 * @class ErrorReponse
 * @desc ErrorReponse return intance
 * @since 2025-01-14
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String errorCode;
    private String errorMessage;
    /*json 대응*/
    private Map data;

    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
