package org.example.response;

/**
 * 定义通用的返回数据结构
 */
public class CommonReturnType {
    // 成功返回success 错误返回fail
    private String status;

    private Object data;

    public static CommonReturnType create(Object result){
        return create(result, "success");
    }

    public static CommonReturnType create(Object result, String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
