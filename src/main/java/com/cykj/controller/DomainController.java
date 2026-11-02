package com.cykj.controller;

import com.cykj.enity.DO.DomainDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.DomainVO;
import com.cykj.enity.vo.UserVO;
import com.cykj.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("domain")
public class DomainController {

    @Autowired
    private DomainService domainService;

    // gljk yhjk领域查询
    @RequestMapping("/allDomain")
    public ResponseDTO SelectDomain(DomainDO domainDO) {
        return ResponseDTO.success(domainService.selectDomain(domainDO));
    }
    // gljk 修改领域
    @RequestMapping("/updateDomain")
    public ResponseDTO UpdateDomain(DomainVO domainVO) {
        domainService.updateDomain(domainVO);
        return ResponseDTO.success("修改成功");
    }
    // gljk 增加领域
    @RequestMapping("/insertDomain")
    public ResponseDTO insertDomain(DomainVO domainVO) {
        Integer i = domainService.insertDomain(domainVO);
        if (i > 0) {
            return ResponseDTO.success();
        }
        return ResponseDTO.fail("该领域已存在");
    }
    // gljk 删除领域
    @RequestMapping("/delDomain")
    public ResponseDTO delDomain(Integer id) {
        domainService.delDomain(id);
        return ResponseDTO.success("删除成功");
    }
}
