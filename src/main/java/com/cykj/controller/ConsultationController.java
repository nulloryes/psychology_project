package com.cykj.controller;

import com.cykj.enity.DO.ReservationDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.ReservationVO;
import com.cykj.enity.vo.SchedulingVO;
import com.cykj.service.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("consultation")
public class ConsultationController {

    @Autowired
    private ConsultationService consultationService;
    //gljk yhjk 查看排班表
    @RequestMapping("/selectTime")
    public ResponseDTO selectScheduling(SchedulingVO schedulingVO) {
        return ResponseDTO.success(consultationService.selectScheduling(schedulingVO));
    }
    // gljk 修改排班时间
    @RequestMapping("/updateTime")
    public ResponseDTO updateTime(SchedulingVO schedulingVO) {
        consultationService.updateScheduling(schedulingVO);
        return ResponseDTO.success("修改成功");
    }
    //gljk yhjk 查看预约
    @RequestMapping("/selectReservation")
    public ResponseDTO selectReservation(ReservationDO reservationDO) {
        return ResponseDTO.success(consultationService.getAllConsultation(reservationDO));
    }
    //gljk yhjk 根据id查看预约
    @RequestMapping("/selectReservationById")
    public ResponseDTO getConsultationById(Integer reservationId) {
        return ResponseDTO.success(consultationService.getConsultationById(reservationId));
    }
    //创建预约
    @RequestMapping("/insertReservation")
    public ResponseDTO insertReservation(ReservationVO reservationVO) {
        return ResponseDTO.success(consultationService.insertConsultation(reservationVO));
    }
    //取消预约(删除)
    @RequestMapping("/delReservation")
    public ResponseDTO deleteReservation(Integer reservationId) {
        Integer i = consultationService.deleteConsultation(reservationId);
        if (i > 0) {
            return ResponseDTO.success(i);
        }else return ResponseDTO.fail("预约已通过或者已完成");
    }
    //书写/评价/改期 预约报告(传金额和回答)
    @RequestMapping("/updateReservation")
    public ResponseDTO updateReservation(ReservationVO reservationVO) {
        Integer i = consultationService.updateConsultation(reservationVO);
        if (i > 0) {
            return ResponseDTO.success("操作完成");
        }
        return ResponseDTO.fail("预约已通过不可修改");
    }
}
