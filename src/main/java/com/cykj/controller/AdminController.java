package com.cykj.controller;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.AdminVO;
import com.cykj.enity.vo.UserVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("admin")
public class AdminController {

    // gljk001 管理员登录
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AdminVO adminVO, HttpSession httpSession) {
        return ResponseDTO.success();
    }
    // gljk002 增加管理员

    // gljk003 修改管理员信息（包括权限，充值与扣款）

    // gljk004 删除管理员
    // gljk005 查询所有管理员（用roleId区分）
    // gljk008 根据id查询管理员


}
