package com.cykj.service.impl;

import com.cykj.enity.DO.DomainDO;
import com.cykj.enity.vo.DomainVO;
import com.cykj.enity.vo.UserVO;
import com.cykj.mapper.DomainMapper;
import com.cykj.service.DomainService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DomainServiceImpl implements DomainService {
    @Autowired
    private DomainMapper domainMapper;

    @Override
    public List<DomainVO> selectDomain(DomainDO domainDO) {
        List<DomainVO> domainVOS = domainMapper.selectDomain(domainDO);
        return domainVOS;
    }

    @Override
    public Integer insertDomain(DomainVO domainVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        domainVO.setCreateTime(now);
        DomainDO domainDO = new DomainDO();
        domainDO.setName(domainVO.getName());
        List<DomainVO> domainVOS = domainMapper.selectDomain(domainDO);
        if(domainVOS.isEmpty()){
            return domainMapper.insertDomain(domainVO);
        }
        return 0;
    }

    @Override
    public Integer updateDomain(DomainVO domainVO) {
        return domainMapper.updateDomain(domainVO);
    }

    @Override
    public Integer delDomain(Integer id) {
        return domainMapper.delDomain(id);
    }
}
