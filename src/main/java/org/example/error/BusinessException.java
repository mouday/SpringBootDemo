package org.example.error;

// 包装器业务异常类实现
public class BusinessException extends Exception implements CommonError{
    private CommonError commonError;

    // 接收 EmBusinessError
    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    // 自定义message
    public BusinessException(CommonError commonError, String message) {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(message);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
