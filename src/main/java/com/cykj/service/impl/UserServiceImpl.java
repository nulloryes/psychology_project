package com.cykj.service.impl;

import com.cykj.enity.DO.UserDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.LogVO;
import com.cykj.enity.vo.UserVO;
import com.cykj.mapper.SystemMapper;
import com.cykj.mapper.UserMapper;
import com.cykj.service.UserService;
import com.cykj.util.JwtUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SystemMapper systemMapper;

    @Override
    public ResponseDTO login(UserVO userVO, HttpSession httpSession) {
        //1.获取验证
        String code = (String) httpSession.getAttribute(userVO.getUnique());
        //2.验证验证码
        if(code==null || !code.equalsIgnoreCase(userVO.getCode())){
            return ResponseDTO.fail(-2,"验证码错误或过期");
        }
        //3.验证完删除标识
        httpSession.removeAttribute(userVO.getUnique());
        //4.数据库查找
        UserVO login = userMapper.login(userVO);
        if(login != null){
            //5.生成令牌
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userId", login.getUserId());
            hashMap.put("name", login.getName());
            String token = JwtUtils.generateJwt(hashMap);
            return ResponseDTO.success(token);
        }
        return ResponseDTO.fail();
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
        return userMapper.selectUserById(id);
    }

    @Override
    public Integer insertUser(UserVO userVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userVO.setCreateTime(now);
        UserDO userDO = new UserDO();
        if(userVO.getAccount()==null){
            return -2;
        }
        userDO.setAccount(userVO.getAccount());
        List<UserVO> userVOS = userMapper.selectAllUser(userDO);
        if(userVOS.size()==1){
            return 0;
        }
        LogVO logVO = new LogVO();
        logVO.setType(1);
        logVO.setCreateTime(now);
        systemMapper.insertLog(logVO);
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
