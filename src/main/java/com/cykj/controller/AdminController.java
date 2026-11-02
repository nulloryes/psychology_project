package com.cykj.controller;

import com.cykj.enity.DO.AdminDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.AdminVO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.UserVO;
import com.cykj.service.AdminService;
import com.cykj.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // gljk001 管理员登录
    @PostMapping("/login")
    public ResponseDTO login(@RequestBody AdminVO adminVO, HttpSession httpSession) {
        return adminService.login(adminVO, httpSession);
    }
    @GetMapping("/getAdminInfo")
    public ResponseDTO getAdminInfo(HttpServletRequest request) {
        String token = request.getHeader("psychology_token");
        Claims claims = JwtUtils.parseJWT(token);
        if (claims.get("adminId") == null) {
            return ResponseDTO.fail(-10, "token类型错误");
        }
        return ResponseDTO.success(claims);
    }
    // gljk002 增加管理员
    @RequestMapping("/insertAdmin")
    public ResponseDTO insertAdmin(ConsultantVO adminVO) {
        Integer i = adminService.insertAdmin(adminVO);
        if (i > 0) {
            return ResponseDTO.success();
        }
        return ResponseDTO.fail("账户已存在");
    }
    // gljk 提交医师信息
    @RequestMapping("/insertConsultant")
    public ResponseDTO insertConsultant(ConsultantVO adminVO) {
        Integer i = adminService.insertConsultant(adminVO);
        if (i > 0) {
            return ResponseDTO.success(i);
        }
        return ResponseDTO.fail("你已申请过了，请等待审核");
    }
    // gljk 审核通过医师
    @RequestMapping("/reviewConsultant")
    public ResponseDTO reviewConsultant(Integer id) {
        adminService.reviewConsultant(id);
        return ResponseDTO.success("提交成功");
    }
    // gljk003 修改管理员信息（包括权限，充值与扣款）
    @RequestMapping("/updateAdmin")
    public ResponseDTO UpdateAdmin(AdminVO adminVO) {
        Integer i = adminService.updateAdmin(adminVO);
        if (i > 0) {
            return ResponseDTO.success("修改成功");
        }
        return ResponseDTO.fail("旧密码验证失败");
    }

    // gljk004 删除管理员
    @RequestMapping("/delAdmin")
    public ResponseDTO delAdmin(Integer id) {
        adminService.deleteAdmin(id);
        return ResponseDTO.success("删除成功");
    }

    // gljk005 查询所有管理员
    @RequestMapping("/allAdmin")
    public ResponseDTO selectAllAdmin(AdminDO adminDO) {
        return ResponseDTO.success(adminService.selectAllAdmin(adminDO));
    }
    // gljk008 根据id查询管理员
    @RequestMapping("/adminInfo")
    public ResponseDTO SelectAllAdminById(Integer id) {
        return ResponseDTO.success(adminService.selectById(id));
    }

}
