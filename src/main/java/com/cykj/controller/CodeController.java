package com.cykj.controller;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.util.ValidateCodeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("code")
public class CodeController {

    @GetMapping("getCode")
    public ResponseDTO getCode(HttpSession httpSession ,String uniqueRequest) {

//        7.为了使缓存不崩，遇到唯一标识不是空的则删除
        if(uniqueRequest != null){
            httpSession.removeAttribute(uniqueRequest);
        }

//        1.创建验证码工具类
        ValidateCodeUtil validateCodeUtil = new ValidateCodeUtil();
//        2.生成验证码图片 ==> base64位的字符串
        String CodeImage = validateCodeUtil.getRandomCodeImage();
//        3.获取验证码答案
        String code = validateCodeUtil.getCode();
//        4.生成唯一标识
        String unique = UUID.randomUUID().toString();
//        5.存储验证码答案
        httpSession.setAttribute(unique,code);
//        6.把验证码和唯一标识返回给前端
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("unique",unique);
        map.put("CodeImage",CodeImage);
        return ResponseDTO.success(map);
    }
}
