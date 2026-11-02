package com.cykj.service.impl;

import com.cykj.enity.DO.RoleDO;
import com.cykj.enity.vo.MenuVO;
import com.cykj.enity.vo.RoleMenuVO;
import com.cykj.enity.vo.RoleVO;
import com.cykj.mapper.RoleMapper;
import com.cykj.mapper.SystemMapper;
import com.cykj.service.RoleService;
import com.cykj.service.SystemService;
import com.cykj.util.TreeMenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private SystemMapper systemMapper;


    @Override
    public List<RoleVO> allRole(RoleVO roleVO) {
            return roleMapper.allRole(roleVO);
    }

    @Override
    public Integer insertRole(RoleVO roleVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        roleVO.setCreateTime(now);
        List<RoleVO> roleVOS = roleMapper.allRole(roleVO);
        if(roleVOS.isEmpty()){
            roleMapper.insertRole(roleVO);
            roleVOS = roleMapper.allRole(roleVO);
            for (int i = 0; i < roleVO.getMenuIds().length; i++) {
                RoleMenuVO roleMenuVO = new RoleMenuVO();
                roleMenuVO.setMenuId(roleVO.getMenuIds()[i]);
                roleMenuVO.setRoleId(roleVOS.get(0).getRoleId());
                roleMenuVO.setCreateTime(now);
                roleMapper.insertRoleMenu(roleMenuVO);
            }
            return 1;
        }
        return 0;
    }

    @Override
    public Integer updateRole(RoleVO roleVO) {
        if(roleVO.getMenuIds()!=null){
            int num;
            RoleMenuVO roleMenuVO = new RoleMenuVO();
            List<RoleMenuVO> vos = roleMapper.selectRoleMenu(roleVO.getRoleId());
            for (int i = 0; i < roleVO.getMenuIds().length; i++) {
                num = 0;
                for (RoleMenuVO vo : vos) {
                    if(vo.getMenuId().equals(roleVO.getMenuIds()[i])){
                        num = 1;
                        break;
                    }
                }
                if (num == 0){
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    roleMenuVO.setMenuId(roleVO.getMenuIds()[i]);
                    roleMenuVO.setRoleId(roleVO.getRoleId());
                    roleMenuVO.setCreateTime(now);
                    roleMapper.insertRoleMenu(roleMenuVO);
                }
            }
            for (RoleMenuVO vo : vos) {
                num = 0;
                for (int i = 0; i < roleVO.getMenuIds().length; i++) {
                    if(vo.getMenuId().equals(roleVO.getMenuIds()[i])){
                        num = 1;
                        break;
                    }
                }
                if (num == 0){
                    roleMenuVO.setMenuId(vo.getMenuId());
                    roleMenuVO.setRoleId(roleVO.getRoleId());
                    roleMapper.delRoleMenu(roleMenuVO);
                }
            }
        }
        return roleMapper.updateRole(roleVO);
    }

    @Override
    public Integer delRole(Integer id) {
        List<RoleMenuVO> vos = roleMapper.selectRoleMenu(id);
        for (RoleMenuVO vo : vos) {
            roleMapper.delRoleMenu(vo);
        }
        return roleMapper.delRole(id);
    }

    @Override
    public List<RoleMenuVO> selectRoleMenu(Integer id) {
        return roleMapper.selectRoleMenu(id);
    }

    @Override
    public HashMap<String,Object> selectDivideTree(Integer id) {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<MenuVO> allMenu = systemMapper.getMenu();
        List<MenuVO> fromData = TreeMenuUtil.getMenus(allMenu);
        List<MenuVO> menus = roleMapper.selectMenus(id);
        List<MenuVO> toData = TreeMenuUtil.getMenus(menus);
        TreeMenuUtil.filterMenus(fromData,menus);
        hashMap.put("fromData", fromData);
        hashMap.put("toData", toData);
        return hashMap;
    }

    @Override
    public Integer insertRoleMenu(RoleMenuVO roleMenuVO) {
        for (int i = 0; i < roleMenuVO.getMenuIds().length; i++) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            roleMenuVO.setCreateTime(now);
            roleMenuVO.setMenuId(roleMenuVO.getMenuIds()[i]);
            roleMapper.insertRoleMenu(roleMenuVO);
        }
        return roleMenuVO.getMenuIds().length;
    }

    @Override
    public Integer updateDivide(RoleMenuVO roleMenuVO) {
        List<Integer> ids = new ArrayList<>();
        for (MenuVO item : roleMenuVO.getMenuList()) {
            ids.add(item.getMenuId());
            if(item.getChildren().size()>0){
                for (MenuVO child : item.getChildren()) {
                    ids.add(child.getMenuId());
                }
            }
        }
        roleMenuVO.setMenuIds(ids.toArray(new Integer[ids.size()]));
        roleMapper.delRoleMenuById(roleMenuVO.getRoleId());
        insertRoleMenu(roleMenuVO);
        return 1;
    }

    @Override
    public Integer delRoleMenu(RoleMenuVO roleMenuVO) {
        for (int i = 0; i < roleMenuVO.getMenuIds().length; i++) {
            roleMenuVO.setMenuId(roleMenuVO.getMenuIds()[i]);
            roleMapper.delRoleMenu(roleMenuVO);
        }
        return roleMenuVO.getMenuIds().length;
    }
}
