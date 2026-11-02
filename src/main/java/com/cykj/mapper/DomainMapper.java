package com.cykj.mapper;

import com.cykj.enity.DO.DomainDO;
import com.cykj.enity.vo.DomainVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainMapper {
    List<DomainVO> selectDomain(@Param("domainDO") DomainDO domainDO);

    Integer insertDomain(@Param("domainVO") DomainVO domainVO);

    Integer updateDomain(@Param("domainVO") DomainVO domainVO);

    Integer delDomain(@Param("id") Integer id);
}
