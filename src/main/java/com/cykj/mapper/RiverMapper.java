package com.cykj.mapper;

import com.cykj.enity.DO.MoneyFlowDO;
import com.cykj.enity.vo.MoneyFlowVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RiverMapper {
    List<MoneyFlowVO> selectRiver(@Param("moneyFlowDO") MoneyFlowDO moneyFlowDO);

    List<MoneyFlowVO> selectRiverByUserId(@Param("userId") Integer userId);

    Integer insertRiver(@Param("moneyFlowVO") MoneyFlowVO moneyFlowVO);

    Integer insertMoney(@Param("moneyFlowVO") MoneyFlowVO moneyFlowVO);

    Integer updateRiver(@Param("moneyFlowVO") MoneyFlowVO moneyFlowVO);
}
