package com.cykj.service.impl;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.UserVO;
import com.cykj.mapper.UserMapper;
import com.cykj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseDTO login(UserVO studentVO, HttpSession session) {
        return ResponseDTO.success();
    }
}
