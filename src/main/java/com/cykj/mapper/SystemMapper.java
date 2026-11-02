package com.cykj.mapper;

import com.cykj.enity.vo.LogVO;
import com.cykj.enity.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SystemMapper {
    Integer insertLog(@Param("logVO") LogVO logVO);

    List<LogVO> selectLog(@Param("logVO") LogVO logVO);

    List<MenuVO> selectMenu();

    List<MenuVO> getMenu();

    Integer insertMenu(@Param("menuVO") MenuVO menuVO);

    Integer updateMenu(@Param("menuVO") MenuVO menuVO);

    Integer menuStatus(@Param("id") Integer id,@Param("status") Integer status);
}
