package com.cykj.controller;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.UserVO;
import com.cykj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    // yhjk001 用户登录
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserVO userVO, HttpSession httpSession) {
        return userService.login(userVO,httpSession);
    }
    // yhjk002 用户注册
    // yhjk003 gljk 根据id查询用户信息
    // yhjk004 修改用户信息(包括充值与扣款)


    // gljk 查询所有用户
    // gljk 删除用户
}
