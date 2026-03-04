package com.cykj.service.impl;

import com.cykj.enity.DO.UserDO;
import com.cykj.enity.vo.UserVO;
import com.cykj.mapper.UserMapper;
import com.cykj.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO login(UserVO userVO, HttpSession session) {
        UserVO user = userMapper.login(userVO);
        return null;
    }

    @Override
    public PageInfo<UserVO> SelectAllUser(UserDO userDO){
        PageHelper.startPage(userDO.getPageNum(),userDO.getPageSize());
        try {
            List<UserVO> userVOS = userMapper.selectAllUser(userDO);
            return new PageInfo<>(userVOS);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public UserVO selectById(Integer id) {
        return userMapper.selectAllUserById(id);
    }

    @Override
    public Integer insertUser(UserVO userVO) {
        UserDO userDO = new UserDO();
        userDO.setAccount(userVO.getAccount());
        List<UserVO> userVOS = userMapper.selectAllUser(userDO);
        if(!userVOS.isEmpty()){
            return 0;
        }
        return userMapper.insertUser(userVO);
    }

    @Override
    public Integer updateUser(UserVO userVO) {
        return userMapper.updateUser(userVO);
    }

    @Override
    public Integer deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }

}
