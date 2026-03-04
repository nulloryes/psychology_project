package com.cykj.mapper;

import com.cykj.enity.DO.UserDO;
import com.cykj.enity.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    UserVO login(@Param("userVO") UserVO userVO);

    List<UserVO> selectAllUser(@Param("userDO") UserDO userDO);

    Integer insertUser(@Param("userVO") UserVO userVO);

    Integer updateUser(@Param("userVO") UserVO userVO);

    Integer deleteUser(@Param("useId") Integer useId);

    UserVO selectAllUserById(@Param("useId") Integer useId);

}
