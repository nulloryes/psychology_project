package com.cykj.service.impl;

import com.cykj.enity.DO.ConsultantDO;
import com.cykj.enity.vo.*;
import com.cykj.mapper.AdminMapper;
import com.cykj.mapper.ConsultantMapper;
import com.cykj.mapper.DomainMapper;
import com.cykj.mapper.SystemMapper;
import com.cykj.service.ConsultantService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ConsultantServiceImpl implements ConsultantService {

    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private SystemMapper systemMapper;

    @Override
    public PageInfo<ConsultantVO> selectAllConsultant(ConsultantDO consultantDO) {
        PageHelper.startPage(consultantDO.getPageNum(), consultantDO.getPageSize());
        try {
            List<ConsultantVO> consultantVOS = consultantMapper.selectAllConsultant(consultantDO);
            return new PageInfo<>(consultantVOS);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public Integer updateConsultant(ConsultantVO consultantVO) {
        if(consultantVO.getDomains()!=null){
            int num;
            ConsultantVO consultant = consultantMapper.selectConsultantById(consultantVO.getConsultantId());
            List<DomainVO> domain = consultant.getDomain();
            for(int i=0;i<consultantVO.getDomains().length;i++){
                num = 0;
                for (DomainVO domainVO : domain) {
                    if (consultantVO.getDomains()[i].equals(domainVO.getDomainId())) {
                        num = 1;
                        break;
                    }
                }
                if (num == 0) {
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    consultantMapper.insertDomain(consultantVO.getConsultantId(), consultantVO.getDomains()[i],now);
                }
            }
            for (DomainVO domainVO : domain) {
                num = 0;
                for (int j = 0; j < consultantVO.getDomains().length; j++) {
                    if (consultantVO.getDomains()[j].equals(domainVO.getDomainId())) {
                        num = 1;
                        break;
                    }
                }
                if (num == 0) {
                    consultantMapper.delDomain(consultantVO.getConsultantId(), domainVO.getDomainId());
                }
            }
        }
        return consultantMapper.updateConsultant(consultantVO);
    }

    @Override
    public ConsultantVO selectConsultantById(Integer consultantId) {
        ConsultantVO consultantVO = consultantMapper.selectConsultantById(consultantId);
        return consultantVO;
    }

    @Override
    public Integer insertScheduling(SchedulingVO schedulingVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(schedulingVO.getAdminId()!=null){
            ConsultantDO consultantDO = new ConsultantDO();
            consultantDO.setAdminId(schedulingVO.getAdminId());
            List<ConsultantVO> vo = consultantMapper.selectAllConsultant(consultantDO);
            schedulingVO.setConsultantId(vo.get(0).getConsultantId());
        }
        schedulingVO.setCreateTime(now);
        return consultantMapper.insertScheduling(schedulingVO);
    }

    @Override
    public Integer reviewConsultation(Integer id) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LogVO logVO = new LogVO();
        logVO.setType(2);
        logVO.setCreateTime(now);
        systemMapper.insertLog(logVO);
        return consultantMapper.reviewConsultation(id);
    }

    @Override
    public Integer deleteConsultant(Integer consultantId) {
        ConsultantVO consultant = consultantMapper.selectConsultantById(consultantId);
        if(consultant==null){
            return 0;
        }
        AdminVO adminVO = new AdminVO();
        adminVO.setAdminId(consultant.getAdminId());
        adminVO.setRoleId(2);
        adminMapper.updateAdmin(adminVO);
        consultantMapper.deleteDomain(consultantId);
        return consultantMapper.deleteConsultant(consultantId);
    }

}
