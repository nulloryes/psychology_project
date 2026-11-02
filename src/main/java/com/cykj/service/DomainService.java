package com.cykj.service;

import com.cykj.enity.DO.DomainDO;
import com.cykj.enity.vo.DomainVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface DomainService {
    List<DomainVO> selectDomain(DomainDO domainDO);

    Integer insertDomain(DomainVO domainVO);

    Integer updateDomain(DomainVO domainVO);

    Integer delDomain(Integer id);
}
