package com.cykj.service;

import com.cykj.enity.DO.MoneyFlowDO;
import com.cykj.enity.vo.MoneyFlowVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RiverService {
    PageInfo<MoneyFlowVO> selectRiver(MoneyFlowDO moneyFlowDO);

    List<MoneyFlowVO> selectRiverByUserId(Integer userId);

    Integer insertRiver(MoneyFlowVO moneyFlowVO);

    Integer insertMoney(MoneyFlowVO moneyFlowVO);

    Integer updateRiver(MoneyFlowVO moneyFlowVO);
}
