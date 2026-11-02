package com.cykj.service;


import com.cykj.enity.DO.ConsultantDO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.ReservationVO;
import com.cykj.enity.vo.SchedulingVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConsultantService {
    PageInfo<ConsultantVO> selectAllConsultant(ConsultantDO consultantDO);

    Integer updateConsultant(ConsultantVO consultantVO);

    ConsultantVO selectConsultantById(Integer consultantId);

    Integer insertScheduling(SchedulingVO schedulingVO);

    Integer reviewConsultation(Integer reservationId);

    Integer deleteConsultant(Integer consultantId);
}
