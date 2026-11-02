package com.cykj.service;

import com.cykj.enity.DO.UserDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.UserVO;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    ResponseDTO login(UserVO studentVO, HttpSession httpSession);

    PageInfo<UserVO> SelectAllUser(UserDO userDO);

    UserVO selectById(Integer id);

    Integer insertUser(UserVO userVO);

    Integer updateUser(UserVO userVO);

    Integer deleteUser(Integer id);
}
