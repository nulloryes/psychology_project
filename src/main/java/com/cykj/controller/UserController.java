package com.cykj.controller;

import com.cykj.enity.DO.UserDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.UserVO;
import com.cykj.service.UserService;
import com.cykj.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;

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
    @GetMapping("/getUserInfo")
    public ResponseDTO getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("psychology_token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims.get("userId") == null) {
            return ResponseDTO.fail(-10, "token类型错误");
        }
        return ResponseDTO.success(claims);
    }
    // yhjk002 用户注册
    @RequestMapping("/enroll")
    public ResponseDTO insertUser(UserVO userVO) {
        Integer i = userService.insertUser(userVO);
        if (i > 0) {
            return ResponseDTO.success();
        } else if(i == -2){
            return ResponseDTO.fail(i,"账户不可为空");
        }
        return ResponseDTO.fail("账户已存在");
    }
    // yhjk003 gljk 根据id查询用户信息
    @RequestMapping("/userInfo")
    public ResponseDTO SelectUserById(Integer id) {
        return ResponseDTO.success(userService.selectById(id));
    }
    // yhjk004 修改用户信息(包括充值与扣款)
    @RequestMapping("/updateUser")
    public ResponseDTO UpdateUser(UserVO userVO) {
        return ResponseDTO.success(userService.updateUser(userVO));
    }

    // gljk 查询所有用户
    @RequestMapping("/allUser")
    public ResponseDTO SelectAllUser(UserDO userDO) {
        return ResponseDTO.success(userService.SelectAllUser(userDO));
    }
    // gljk 删除用户
    @RequestMapping("/delUser")
    public ResponseDTO delUser(Integer id) {
        userService.deleteUser(id);
        return ResponseDTO.success("删除成功");
    }
}
