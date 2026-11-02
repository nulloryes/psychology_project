package com.cykj.service;

import com.cykj.enity.vo.LogVO;
import com.cykj.enity.vo.MenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemService{
    Integer insertLog(LogVO logVO);

    List<LogVO> selectLog(LogVO logVO);

    List<MenuVO> selectMenu();

    List<MenuVO> getMenus();

    Integer insertMenu(MenuVO menuVO);

    Integer updateMenu(MenuVO menuVO);

    Integer menuStatus(Integer id,Integer status);
}
