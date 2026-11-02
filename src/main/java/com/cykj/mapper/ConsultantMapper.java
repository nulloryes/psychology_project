package com.cykj.mapper;

import com.cykj.enity.DO.ConsultantDO;
import com.cykj.enity.DO.DomainDO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.DomainVO;
import com.cykj.enity.vo.SchedulingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ConsultantMapper {
    List<ConsultantVO> selectAllConsultant(@Param("consultantDO") ConsultantDO consultantDO);
    List<DomainVO> selectDomain(@Param("consultantId") Integer consultantId);

    Integer updateConsultant(@Param("consultantVO") ConsultantVO consultantVO);
    Integer insertDomain(@Param("consultantId") Integer consultantId, @Param("domainId") Integer domainId, @Param("time")Timestamp createTime);
    Integer delDomain(@Param("consultantId") Integer consultantId, @Param("domainId") Integer domainId);

    ConsultantVO selectConsultantById(@Param("consultantId") Integer consultantId);

    Integer insertScheduling(@Param("schedulingVO")SchedulingVO schedulingVO);

    Integer reviewConsultation(@Param("reservationId")Integer reservationId);

    Integer deleteConsultant(@Param("consultantId") Integer consultantId);
    Integer deleteDomain(@Param("consultantId") Integer consultantId);

    Integer updateRateScore(@Param("consultantId") Integer consultantId, @Param("rateScore") Integer rateScore);
}
