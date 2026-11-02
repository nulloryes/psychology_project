package com.cykj.mapper;

import com.cykj.enity.DO.ReservationDO;
import com.cykj.enity.vo.ReservationVO;
import com.cykj.enity.vo.SchedulingVO;
import com.cykj.enity.vo.list.reservedVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConsultationMapper {
    List<reservedVO> selectReserved(@Param("consultantId") Integer consultantId);
    Integer deleteScheduling(@Param("consultantId") Integer consultantId);
    List<SchedulingVO> selectScheduling(@Param("consultantId") Integer consultantId);

    Integer updateScheduling(@Param("schedulingVO")SchedulingVO schedulingVO);

    Integer updateConsultation(@Param("reservationVO") ReservationVO reservationVO);

    List<ReservationVO> getAllConsultation(@Param("reservationDO") ReservationDO reservationDO);

    ReservationVO getConsultationById(@Param("reservationId") Integer reservationId);

    Integer insertConsultation(@Param("reservationVO") ReservationVO reservationVO);

    Integer deleteConsultation(@Param("reservationId") Integer reservationId);

    Double getAvgRateScore(@Param("consultantId") Integer consultantId);
}
