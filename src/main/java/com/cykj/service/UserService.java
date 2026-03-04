package com.cykj.service;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.UserVO;

import javax.servlet.http.HttpSession;

public interface UserService {
    ResponseDTO login(UserVO studentVO, HttpSession session);
}
