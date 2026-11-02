package com.cykj.service.impl;

import com.cykj.enity.DO.ConsultantDO;
import com.cykj.enity.DO.ReservationDO;
import com.cykj.enity.vo.*;
import com.cykj.enity.vo.list.reservedVO;
import com.cykj.mapper.ConsultantMapper;
import com.cykj.mapper.ConsultationMapper;
import com.cykj.mapper.RiverMapper;
import com.cykj.mapper.SystemMapper;
import com.cykj.service.ConsultationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {
    @Autowired
    private ConsultationMapper consultationMapper;
    @Autowired
    private RiverMapper riverMapper;
    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private SystemMapper systemMapper;

    @Override
    public Integer updateConsultation(ReservationVO reservationVO) {
        ReservationVO vo = consultationMapper.getConsultationById(reservationVO.getReservationId());
        if(reservationVO.getAnswer()!=null){
            ConsultantVO consultantVO = consultantMapper.selectConsultantById(reservationVO.getConsultantId());
            reservationVO.setMoney(consultantVO.getPrice());
            reservationVO.setStatus(2);
            MoneyFlowVO moneyFlowVO = new MoneyFlowVO();
            moneyFlowVO.setUserId(reservationVO.getUserId());
            moneyFlowVO.setConsultantId(reservationVO.getConsultantId());
            moneyFlowVO.setTip("心理咨询");
            moneyFlowVO.setMoney(reservationVO.getMoney());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            moneyFlowVO.setCreateTime(now);
            riverMapper.insertRiver(moneyFlowVO);
            LogVO logVO = new LogVO();
            logVO.setType(3);
            logVO.setCreateTime(now);
            systemMapper.insertLog(logVO);
        }else if(reservationVO.getRate()!=null){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            reservationVO.setRateTime(String.valueOf(now));
            // 先保存评价到预约记录
            consultationMapper.updateConsultation(reservationVO);
            // 计算该咨询师所有评价的平均分并更新consultant表的rate_score
            Double avgScore = consultationMapper.getAvgRateScore(vo.getConsultantId());
            if (avgScore != null) {
                int roundedScore = (int) Math.round(avgScore);
                consultantMapper.updateRateScore(vo.getConsultantId(), roundedScore);
            }
            return 1;
        }else if (reservationVO.getOrderTime()==null && vo.getStatus()==1||vo.getStatus()==2) {
            return 0;
        }
        return consultationMapper.updateConsultation(reservationVO);
    }

    @Override
    public HashMap<String,Object> selectScheduling(SchedulingVO vo) {
        HashMap<String, Object> hashMap = new HashMap<>();
        if(vo.getAdminId()!=null){
            ConsultantDO consultantDO = new ConsultantDO();
            consultantDO.setAdminId(vo.getAdminId());
            List<ConsultantVO> vos = consultantMapper.selectAllConsultant(consultantDO);
            vo.setConsultantId(vos.get(0).getConsultantId());
        }
        List<SchedulingVO> schedulingVO = consultationMapper.selectScheduling(vo.getConsultantId());
        List<reservedVO> reservedVOS = consultationMapper.selectReserved(vo.getConsultantId());
        hashMap.put("reservedList",reservedVOS);
        hashMap.put("schedulingList",schedulingVO);
        return hashMap;
    }

    @Override
    public PageInfo<ReservationVO> getAllConsultation(ReservationDO reservationDO) {
        if (reservationDO.getPageNum() == null || reservationDO.getPageSize() == null || reservationDO.getPageNum() <= 0) {
            List<ReservationVO> allConsultation = consultationMapper.getAllConsultation(reservationDO);
            PageInfo<ReservationVO> reservationVOPageInfo = new PageInfo<>(allConsultation);
            reservationVOPageInfo.setList(allConsultation);
            return reservationVOPageInfo;
        }
        PageHelper.startPage(reservationDO.getPageNum(), reservationDO.getPageSize());
        try {
            List<ReservationVO> allConsultation = consultationMapper.getAllConsultation(reservationDO);
            return new PageInfo<>(allConsultation);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public ReservationVO getConsultationById(Integer reservationId) {
        return consultationMapper.getConsultationById(reservationId);
    }

    @Override
    public Integer insertConsultation(ReservationVO reservationVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        reservationVO.setCreateTime(now);
        LogVO logVO = new LogVO();
        logVO.setType(3);
        logVO.setCreateTime(now);
        systemMapper.insertLog(logVO);
        return consultationMapper.insertConsultation(reservationVO);
    }

    @Override
    public Integer deleteConsultation(Integer reservationId) {
        ReservationVO vo = consultationMapper.getConsultationById(reservationId);
        if (vo.getStatus()==1 || vo.getStatus()==2 || vo.getStatus()==3) {
            return 0;
        }
        return consultationMapper.deleteConsultation(reservationId);
    }

    @Override
    public Integer updateScheduling(SchedulingVO schedulingVO) {
        return consultationMapper.updateScheduling(schedulingVO);
    }
}
