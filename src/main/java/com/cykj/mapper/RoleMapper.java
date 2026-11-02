package com.cykj.mapper;

import com.cykj.enity.DO.RoleDO;
import com.cykj.enity.vo.MenuVO;
import com.cykj.enity.vo.RoleMenuVO;
import com.cykj.enity.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    List<RoleVO> allRole(@Param("roleVO") RoleVO roleVO);

    Integer insertRole(@Param("roleVO") RoleVO roleVO);

    Integer updateRole(@Param("roleVO") RoleVO roleVO);

    Integer delRole(@Param("id") Integer id);

    List<RoleMenuVO> selectRoleMenu(@Param("id") Integer id);
    List<MenuVO> selectMenus(@Param("id") Integer id);

    Integer insertRoleMenu(@Param("roleMenuVO") RoleMenuVO roleMenuVO);

    Integer delRoleMenuById(@Param("id") Integer id);

    Integer delRoleMenu(@Param("roleMenuVO") RoleMenuVO roleMenuVO);
}
