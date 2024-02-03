package com.back.util.response;

public enum ResponseStatusEnum {
    SUCCESS(0,"请求处理成功"),
    FAIL(1,"请求处理失败");
    public final Integer code;
    public final String msg;

    ResponseStatusEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }


}
