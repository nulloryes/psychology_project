package com.cykj.service;

import com.cykj.enity.DO.RoleDO;
import com.cykj.enity.vo.RoleMenuVO;
import com.cykj.enity.vo.RoleVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface RoleService {
    List<RoleVO> allRole(RoleVO roleVO);

    Integer insertRole(RoleVO roleVO);

    Integer updateRole(RoleVO roleVO);

    Integer delRole(Integer id);

    List<RoleMenuVO> selectRoleMenu(Integer id);

    HashMap<String,Object> selectDivideTree(Integer id);

    Integer insertRoleMenu(RoleMenuVO roleMenuVO);

    Integer updateDivide(RoleMenuVO roleMenuVO);

    Integer delRoleMenu(RoleMenuVO roleMenuVO);
}
