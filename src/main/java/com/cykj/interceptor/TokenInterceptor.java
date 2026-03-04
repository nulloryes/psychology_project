package com.cykj.interceptor;

import com.alibaba.fastjson.JSON;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.util.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    //暂时只用前置拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("我被拦截了。。。。");

        //获取请求头中的token
        String token = request.getHeader("omo2411_token");
        //1.判断前端有没有携带token
        if (token == null || token.equals("")) {
            //组装dto
            ResponseDTO responseDTO = new ResponseDTO(-10, "请携带token", null);
            response.setContentType("text/html;charset=utf-8");
            //响应
            response.getWriter().write(JSON.toJSONString(responseDTO));
            return false;
        }
        //2.判断token有没有过期
        try {
            JwtUtils.parseJWT(token);
        } catch (Exception e) {
            //组装dto
            ResponseDTO responseDTO = new ResponseDTO(-10, "token已过期", null);
            response.setContentType("text/html;charset=utf-8");
            //响应
            response.getWriter().write(JSON.toJSONString(responseDTO));
            return false;
        }
        return true;
    }
}
