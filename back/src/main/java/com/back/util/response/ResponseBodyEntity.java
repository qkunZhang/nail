package com.back.util.response;

import lombok.Data;

@Data
public class ResponseBodyEntity<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> ResponseBodyEntity<T> success(T data){
        ResponseBodyEntity<T> responseBody = new ResponseBodyEntity<>();
        responseBody.setCode(ResponseStatusEnum.SUCCESS.code);
        responseBody.setMsg(ResponseStatusEnum.SUCCESS.msg);
        responseBody.setData(data);
        return responseBody;
    }

    public static <T> ResponseBodyEntity<T> fail(String msg){
        ResponseBodyEntity<T> responseBody = new ResponseBodyEntity<>();
        responseBody.setCode(ResponseStatusEnum.FAIL.code);
        responseBody.setMsg(msg);
        return responseBody;
    }


}

