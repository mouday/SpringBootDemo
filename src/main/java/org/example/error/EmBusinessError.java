package org.example.error;

public enum EmBusinessError implements CommonError {
    // 1000开头为通用错误码
    UNKNOWN_ERROR(10000, "未知错误"),
    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),

    // 2000开头为用户信息错误
    USER_NOT_EXIST(20001, "用户不存在"),
    USER_LOGIN_FAIL(20002, "用户手机号或密码不正确"),
    USER_NOT_LOGIN(20003, "用户未登录"),

    // 3000开头为交易信息错误
    STOCK_NOT_ENOUGH(30001, "库存不足")
    ;


    private int errCode;
    private String errMsg;

    EmBusinessError(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
