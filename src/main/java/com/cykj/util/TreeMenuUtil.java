package com.cykj.util;

import com.cykj.enity.vo.MenuVO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeMenuUtil {
    public TreeMenuUtil() {}

    public static List<MenuVO> getMenus(List<MenuVO> menus) {
        List<MenuVO> treeMenu = new ArrayList<>();
        for (MenuVO menu : menus) {
            if(menu.getUpId() == 0){
                MenuVO menuChildren = getMenuChildren(menu, menus);
                treeMenu.add(menuChildren);
            }
        }
        return treeMenu;
    }
    private static MenuVO getMenuChildren(MenuVO menuVO,List<MenuVO> menuList){
        for (MenuVO menu : menuList) {
            if(menuVO.getMenuId().equals(menu.getUpId())){
                MenuVO menuSon = getMenuChildren(menu, menuList);
                menuVO.getChildren().add(menuSon);
            }
        }
        return menuVO;
    }

    //allMenu为所有菜单的树形数据，menuList为已分配的菜单列表
    public static void filterMenus(List<MenuVO> allMenu,List<MenuVO> menuList){
        //迭代器
        Iterator<MenuVO> it = allMenu.iterator();
        while(it.hasNext()){
            MenuVO menu = it.next();
            if(menu.getChildren().size() == 0){
                for (MenuVO item : menuList) {
                    if(menu.getMenuId() == item.getMenuId()){
                        it.remove();
                    }
                }
            }else {
                filterMenus(menu.getChildren(), menuList);
                if (menu.getChildren().size()==0){
                    it.remove();
                }
//                for (MenuVO child : menu.getChildren()) {
//                    for (MenuVO item : menuList) {
//                        if(child.getMenuId() == item.getMenuId()){
//                            it.remove();
//                            if (menu.getChildren().size()==0){
//                                it.remove();
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}
