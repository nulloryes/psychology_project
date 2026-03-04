package com.cykj.controller;

import com.cykj.enity.DO.UserDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.UserVO;
import com.cykj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    // yhjk001 用户登录
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody UserVO userVO, HttpSession httpSession) {
        return ResponseDTO.success(userService.login(userVO,httpSession));
    }
    // yhjk002 用户注册
    @RequestMapping("/insertUser")
    public ResponseDTO insertUser(UserVO userVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userVO.setCreateTime(now);
        Integer i = userService.insertUser(userVO);
        if (i > 0) {
            return ResponseDTO.success();
        }
        return ResponseDTO.fail("账户已存在");
    }
    // yhjk003 gljk 根据id查询用户信息
    @RequestMapping("/userInfo")
    public ResponseDTO SelectAllUserById(Integer id) {
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
