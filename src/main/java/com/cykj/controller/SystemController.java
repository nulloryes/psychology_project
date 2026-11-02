package com.cykj.controller;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.LogVO;
import com.cykj.enity.vo.MenuVO;
import com.cykj.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system")
public class SystemController {
    @Autowired
    private SystemService systemService;

    // gljk 日志插入
    @RequestMapping("insertLog")
    public ResponseDTO insertLog(LogVO logVO) {
        return ResponseDTO.success(systemService.insertLog(logVO));
    }
    // gljk 查询日志
    @RequestMapping("selectLog")
    public ResponseDTO selectLog(LogVO logVO) {
        return ResponseDTO.success(systemService.selectLog(logVO));
    }

    // gljk 菜单查询
    @RequestMapping("selectMenu")
    public ResponseDTO selectMenu() {
        return ResponseDTO.success(systemService.selectMenu());
    }

    // gljk 目录菜单
    @RequestMapping("Menus")
    public ResponseDTO getMenus() {
        return ResponseDTO.success(systemService.getMenus());
    }

    // gljk 菜单增加
    @RequestMapping("addMenu")
    public ResponseDTO addMenu(MenuVO menuVO) {
        systemService.insertMenu(menuVO);
        return ResponseDTO.success("添加成功");
    }
    // gljk 菜单修改
    @RequestMapping("updateMenu")
    public ResponseDTO updateMenu(MenuVO menuVO) {
        systemService.updateMenu(menuVO);
        return ResponseDTO.success("修改成功");
    }
    // gljk 菜单启用禁用
    @RequestMapping("menuStatus")
    public ResponseDTO menuStatus(Integer id,Integer status) {
        systemService.menuStatus(id,status);
        return ResponseDTO.success("已禁用");
    }
}
