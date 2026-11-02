package com.cykj.service.impl;

import com.cykj.enity.DO.MoneyFlowDO;
import com.cykj.enity.vo.AdminVO;
import com.cykj.enity.vo.ConsultantVO;
import com.cykj.enity.vo.MoneyFlowVO;
import com.cykj.enity.vo.UserVO;
import com.cykj.mapper.AdminMapper;
import com.cykj.mapper.ConsultantMapper;
import com.cykj.mapper.RiverMapper;
import com.cykj.mapper.UserMapper;
import com.cykj.service.RiverService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RiverServiceImpl implements RiverService {
    @Autowired
    private RiverMapper riverMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public PageInfo<MoneyFlowVO> selectRiver(MoneyFlowDO moneyFlowDO) {
        PageHelper.startPage(moneyFlowDO.getPageNum(),moneyFlowDO.getPageSize());
        try {
            List<MoneyFlowVO> rivers = riverMapper.selectRiver(moneyFlowDO);
            return new PageInfo<>(rivers);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public List<MoneyFlowVO> selectRiverByUserId(Integer userId) {
        UserVO userVO = userMapper.selectUserById(userId);
        if(userVO==null){
            return null;
        }
        return riverMapper.selectRiverByUserId(userId);
    }

    @Override
    public Integer insertRiver(MoneyFlowVO moneyFlowVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        moneyFlowVO.setCreateTime(now);
        UserVO userVO = userMapper.selectUserById(moneyFlowVO.getUserId());
        if(userVO==null){
            return null;
        }
        ConsultantVO consultantVO = consultantMapper.selectConsultantById(moneyFlowVO.getConsultantId());
        if(consultantVO==null){
            return null;
        }
        return riverMapper.insertRiver(moneyFlowVO);
    }

    @Override
    public Integer insertMoney(MoneyFlowVO moneyFlowVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        moneyFlowVO.setCreateTime(now);
        UserVO userVO = userMapper.selectUserById(moneyFlowVO.getUserId());
        if(userVO==null){
            return 0;
        }
        riverMapper.insertRiver(moneyFlowVO);
        moneyFlowVO.setMoney(moneyFlowVO.getMoney()+userVO.getMoney());
        return riverMapper.insertMoney(moneyFlowVO);
    }

    @Override
    public Integer updateRiver(MoneyFlowVO moneyFlowVO) {
        AdminVO adminVO = new AdminVO();
        if(moneyFlowVO.getType().equals("钱包")){
            UserVO userVO = userMapper.selectUserById(moneyFlowVO.getUserId());
            if(userVO.getMoney()>=moneyFlowVO.getMoney()){
                userVO.setMoney(userVO.getMoney()-moneyFlowVO.getMoney());
                userMapper.updateUser(userVO);
            }else {
                return 0;
            }
        }
        Integer consultantId = moneyFlowVO.getConsultantId();
        if(consultantId==null || consultantId==0){
            adminVO.setAdminId(1);
        }else {
            ConsultantVO consultant = consultantMapper.selectConsultantById(consultantId);
            adminVO.setAdminId(consultant.getAdminId());
        }
        AdminVO admin = adminMapper.selectAdminById(adminVO.getAdminId());
        adminVO.setMoney(admin.getMoney()+moneyFlowVO.getMoney());
        moneyFlowVO.setStatus(1);
        adminMapper.updateAdmin(adminVO);
        return riverMapper.updateRiver(moneyFlowVO);
    }
}
