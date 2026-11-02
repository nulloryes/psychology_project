package com.cykj.controller;

import com.cykj.enity.DO.MoneyFlowDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.MoneyFlowVO;
import com.cykj.service.RiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("river")
public class RiverController {
    @Autowired
    private RiverService riverService;

    //gljk yhjk 订单查询
    @RequestMapping("/selectRiver")
    public ResponseDTO selectRiver(MoneyFlowDO moneyFlowDO) {
        return ResponseDTO.success(riverService.selectRiver(moneyFlowDO));
    }
    //yhjk gljk 根据用户id查询消费
    @RequestMapping("/selectRiverByUserId")
    public ResponseDTO selectRiverByUserId(Integer userId) {
        return ResponseDTO.success(riverService.selectRiverByUserId(userId));
    }
    //gljk 新建订单（传id,service判断扣减与增加）
    @RequestMapping("/insertRiver")
    public ResponseDTO insertRiver(MoneyFlowVO moneyFlowVO) {
        riverService.insertRiver(moneyFlowVO);
        return ResponseDTO.success("交易成功");
    }
    //yhjk 充值金额
    @RequestMapping("/insertMoney")
    public ResponseDTO insertMoney(MoneyFlowVO moneyFlowVO) {
        Integer i = riverService.insertMoney(moneyFlowVO);
        if (i > 0) {
            return ResponseDTO.success("充值成功");
        }
        return ResponseDTO.fail("充值失败，尚未登录");
    }
    //yhjk 支付订单(如果不是心理咨询要给id传0，要传金额过来)
    @RequestMapping("/pay")
    public ResponseDTO pay(MoneyFlowVO moneyFlowVO) {
        Integer i = riverService.updateRiver(moneyFlowVO);
        if (i<=0){
            return ResponseDTO.fail("支付失败，可能是余额不足");
        }
        return ResponseDTO.success("支付成功");
    }
}
