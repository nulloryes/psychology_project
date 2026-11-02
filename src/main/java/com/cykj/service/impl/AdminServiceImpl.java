package com.cykj.service.impl;

import com.cykj.enity.DO.AdminDO;
import com.cykj.enity.DO.ConsultantDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.AdminVO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.LogVO;
import com.cykj.mapper.AdminMapper;
import com.cykj.mapper.ConsultantMapper;
import com.cykj.mapper.SystemMapper;
import com.cykj.service.AdminService;
import com.cykj.util.JwtUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ConsultantMapper consultantMapper;

    @Override
    public ResponseDTO login(AdminVO adminVO, HttpSession httpSession) {
        String code = (String) httpSession.getAttribute(adminVO.getUnique());
        if(code==null || !code.equalsIgnoreCase(adminVO.getCode())){
            return ResponseDTO.fail(-2,"验证码错误或过期");
        }
        httpSession.removeAttribute(adminVO.getUnique());
        AdminVO login = adminMapper.login(adminVO);
        if(login != null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("adminId",login.getAdminId());
            hashMap.put("name",login.getName());
            hashMap.put("roleId",login.getRoleId());
            String token = JwtUtils.generateJwt(hashMap);
            return ResponseDTO.success(token);
        }
        return ResponseDTO.fail();
    }

    @Override
    public PageInfo<AdminVO> selectAllAdmin(AdminDO adminDO) {
        PageHelper.startPage(adminDO.getPageNum(), adminDO.getPageSize());
        try {
            List<AdminVO> list = adminMapper.selectAllAdmin(adminDO);
            return new PageInfo<>(list);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public AdminVO selectById(Integer id) {
        return adminMapper.selectAdminById(id);
    }

    @Override
    public Integer deleteAdmin(Integer id) {
        ConsultantDO item = new ConsultantDO();
        item.setDomainId(id);
        List<ConsultantVO> consultant = consultantMapper.selectAllConsultant(item);
        if(!consultant.isEmpty()){
            consultantMapper.deleteConsultant(consultant.get(0).getConsultantId());
        }
        return adminMapper.deleteAdmin(id);
    }

    @Override
    public Integer insertAdmin(ConsultantVO adminVO) {
        AdminDO adminDO = new AdminDO();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        adminVO.setCreateTime(now);
        adminDO.setAccount(adminVO.getAccount());
        List<AdminVO> adminVOS = adminMapper.selectAllAdmin(adminDO);
        if(!adminVOS.isEmpty()){
            return 0;
        }
        return adminMapper.insertAdmin(adminVO);
    }

    @Override
    public Integer insertConsultant(ConsultantVO adminVO) {
        ConsultantDO consultantDO = new ConsultantDO();
        consultantDO.setName(adminVO.getName());
        consultantDO.setStatus(1);
        List<ConsultantVO> consultant = consultantMapper.selectAllConsultant(consultantDO);
        if(!consultant.isEmpty()){
            return 0;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        adminVO.setCreateTime(now);
        adminMapper.insertConsultant(adminVO);
        consultant = consultantMapper.selectAllConsultant(consultantDO);
        Integer consultantId = consultant.get(0).getConsultantId();
        if(adminVO.getDomains()!=null){
            for (int i = 0; i < adminVO.getDomains().length; i++) {
                consultantMapper.insertDomain(consultantId,adminVO.getDomains()[i],now);
            }
        }
        return 1;
    }

    @Override
    public Integer reviewConsultant(Integer id) {
        ConsultantVO consultant = consultantMapper.selectConsultantById(id);
        int adminId = consultant.getAdminId();
        return adminMapper.reviewConsultant(adminId);
    }

    @Override
    public Integer updateAdmin(AdminVO adminVO) {
        if(adminVO.getPassword()!=null){
            AdminVO admin = adminMapper.selectAdminById(adminVO.getAdminId());
            if(!admin.getPassword().equals(adminVO.getOldPassword())){
                return 0;
            }
        }
        return adminMapper.updateAdmin(adminVO);
    }
}
