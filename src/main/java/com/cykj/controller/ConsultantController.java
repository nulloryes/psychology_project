package com.cykj.controller;

import com.cykj.enity.DO.ConsultantDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.SchedulingVO;
import com.cykj.service.ConsultantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("consultant")
public class ConsultantController {

    @Autowired
    private ConsultantService consultantService;

    // gljk006 yhjk005 查询所有咨询师
    @RequestMapping("/allConsultant")
    public ResponseDTO SelectAllConsultant(ConsultantDO consultantDO) {
        return ResponseDTO.success(consultantService.selectAllConsultant(consultantDO));
    }
    // gljk007 yhjk006 根据id查询咨询师
    @RequestMapping("/consultantInfo")
    public ResponseDTO SelectConsultantById(Integer id) {
        return ResponseDTO.success(consultantService.selectConsultantById(id));
    }
    // gljk 修过咨询师信息
    @RequestMapping("/updateConsultant")
    public ResponseDTO UpdateUser(ConsultantVO consultantVO) {
        return ResponseDTO.success(consultantService.updateConsultant(consultantVO));
    }
    // gljk 删除咨询师信息
    @RequestMapping("/delConsultant")
    public ResponseDTO deleteConsultant(Integer id) {
        Integer i = consultantService.deleteConsultant(id);
        if (i > 0) {
            return ResponseDTO.success();
        }
        return ResponseDTO.fail("无此咨询师信息");
    }
    // gljk 预约时间设置
    @RequestMapping("/setTime")
    public ResponseDTO setTime(SchedulingVO schedulingVO) {
        return ResponseDTO.success(consultantService.insertScheduling(schedulingVO));
    }
    // gljk 审核预约
    @RequestMapping("/reviewReservation")
    public ResponseDTO updateReservation(Integer id) {
        Integer i = consultantService.reviewConsultation(id);
        if (i >=0) {
            return ResponseDTO.success("操作成功");
        }
        return ResponseDTO.fail("该预约已操作，不可重复操作");
    }

    //查询培训
    //查询结果

}
