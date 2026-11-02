package com.cykj.controller;

import com.cykj.enity.DO.RoleDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.RoleMenuVO;
import com.cykj.enity.vo.RoleVO;
import com.cykj.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    //gljk 新增角色
    @RequestMapping("/insertRole")
    public ResponseDTO insertRole(RoleVO roleVO) {
        Integer i = roleService.insertRole(roleVO);
        if (i > 0) {
            return ResponseDTO.success();
        }
        return ResponseDTO.fail("该角色已存在");
    }
    //gljk 修改角色
    @RequestMapping("/updateRole")
    public ResponseDTO UpdateRole(RoleVO roleVO) {
        roleService.updateRole(roleVO);
        return ResponseDTO.success("修改成功");
    }
    //gljk 查询所有角色
    @RequestMapping("/allRole")
    public ResponseDTO SelectRole(RoleVO roleVO) {
        return ResponseDTO.success(roleService.allRole(roleVO));
    }
    //gljk 禁用角色
    @RequestMapping("/delRole")
    public ResponseDTO delRole(Integer id) {
        roleService.delRole(id);
        return ResponseDTO.success("删除成功");
    }

    //gljk 查看分配角色能展示的页面
    @RequestMapping("/selectDivide")
    public ResponseDTO selectDivide(Integer roleId) {
        return ResponseDTO.success(roleService.selectRoleMenu(roleId));
    }
    //gljk 查看分配角色能展示的树形数据
    @RequestMapping("/selectDivideTree")
    public ResponseDTO selectDivideTree(Integer roleId) {
        return ResponseDTO.success(roleService.selectDivideTree(roleId));
    }
    //gljk 分配角色能展示的页面
    @RequestMapping("/insertDivide")
    public ResponseDTO insertDivide(RoleMenuVO roleMenuVO) {
        roleService.insertRoleMenu(roleMenuVO);
        return ResponseDTO.success("插入成功");
    }
    //gljk 更改角色能展示的页面
    @PostMapping("/updateDivide")
    public ResponseDTO updateDivide(@RequestBody RoleMenuVO roleMenuVO) {
        roleService.updateDivide(roleMenuVO);
        return ResponseDTO.success("修改成功");
    }
    //gljk 删除分配页面
    @RequestMapping("/delDivide")
    public ResponseDTO delDivide(RoleMenuVO roleMenuVO) {
        roleService.delRoleMenu(roleMenuVO);
        return ResponseDTO.success("删除成功");
    }
}
