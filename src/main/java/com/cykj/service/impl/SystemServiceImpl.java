package com.cykj.service.impl;

import com.cykj.enity.vo.LogVO;
import com.cykj.enity.vo.MenuVO;
import com.cykj.mapper.SystemMapper;
import com.cykj.service.SystemService;
import com.cykj.util.TreeMenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SystemMapper systemMapper;

    @Override
    public Integer insertLog(LogVO logVO) {
        return systemMapper.insertLog(logVO);
    }

    @Override
    public List<LogVO> selectLog(LogVO logVO) {
        return systemMapper.selectLog(logVO);
    }

    @Override
    public List<MenuVO> selectMenu() {
        return systemMapper.selectMenu();
    }

    @Override
    public List<MenuVO> getMenus() {
        List<MenuVO> menus = systemMapper.getMenu();
        return TreeMenuUtil.getMenus(menus);
 }

    @Override
    public Integer insertMenu(MenuVO menuVO) {
        return systemMapper.insertMenu(menuVO);
    }

    @Override
    public Integer updateMenu(MenuVO menuVO) {
        return systemMapper.updateMenu(menuVO);
    }

    @Override
    public Integer menuStatus(Integer id,Integer status) {
        return systemMapper.menuStatus(id,status);
    }
}
