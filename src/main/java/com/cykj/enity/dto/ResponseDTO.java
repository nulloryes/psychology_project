package com.cykj.enity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private Integer code;
    private String msg;
    private Object data;

    private static final Integer SUCCESS = 1;
    private static final Integer FAIL = -1;

    public static ResponseDTO success() {
        return new ResponseDTO(SUCCESS, "success", null);
    }

    public static ResponseDTO success(Object data) {
        return new ResponseDTO(SUCCESS, "success", data);
    }

    public static ResponseDTO fail(){
        return new ResponseDTO(FAIL, "fail", null);
    }

    public static ResponseDTO fail(String msg){
        return new ResponseDTO(FAIL, msg, null);
    }
    public static ResponseDTO fail(Integer code,String msg){
        return new ResponseDTO(code, msg, null);
    }
}
