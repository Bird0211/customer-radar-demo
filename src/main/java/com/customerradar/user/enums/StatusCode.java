package com.customerradar.user.enums;

/**
 * Error Code
 */
public enum StatusCode {

    SUCCESS(0, "SUCCESS"),
    FAIL(1, "FAIL"),
    SYS_ERROR(11111, "Sys Error"),
    DB_ERROR(118001, "DB Error"),
    USER_NOT_EXIST(118002,"User Not Exist"),
    PARAM_ERROR(118003,"Param Error"),
    AUTH_FAIL(118006,"Auth Fail"),
    ;

    private int code;

    private String codeMsg;

    StatusCode(int code, String codeMsg) {
        this.code = code;
        this.codeMsg = codeMsg;
    }

    public int getCode() {
        return code;
    }

    public String getCodeMsg() {
        return codeMsg;
    }

}
