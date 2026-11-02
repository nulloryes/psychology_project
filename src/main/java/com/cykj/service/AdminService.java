package com.cykj.service;

import com.cykj.enity.DO.AdminDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.AdminVO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.UserVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface AdminService {
    ResponseDTO login(AdminVO adminVO, HttpSession httpSession);

    PageInfo<AdminVO> selectAllAdmin(AdminDO adminDO);

    AdminVO selectById(Integer id);

    Integer deleteAdmin(Integer id);

    Integer insertAdmin(ConsultantVO adminVO);

    Integer insertConsultant(ConsultantVO adminVO);

    Integer reviewConsultant(Integer adminId);

    Integer updateAdmin(AdminVO adminVO);
}
