package com.cykj.service;

import com.cykj.enity.DO.ReservationDO;
import com.cykj.enity.vo.ReservationVO;
import com.cykj.enity.vo.SchedulingVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface ConsultationService {
    Integer updateConsultation(ReservationVO reservationVO);

    HashMap<String,Object> selectScheduling(SchedulingVO schedulingVO);

    Integer updateScheduling(SchedulingVO schedulingVO);

    PageInfo<ReservationVO> getAllConsultation(ReservationDO reservationDO);

    ReservationVO getConsultationById(Integer reservationId);

    Integer insertConsultation(ReservationVO reservationVO);

    Integer deleteConsultation(Integer reservationId);
}
