package com.cykj.controller;

import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.AiDifyVO;
import com.cykj.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ai")
public class AIController {

    @Autowired
    private AiService aiService;

    // gljk ai运用(只查出status=2的)
    @RequestMapping("/selectAiByStatus")
    public ResponseDTO selectAiByStatus() {
        return ResponseDTO.success(aiService.selectAiByStatus());
    }

    // gljk ai信息查询
    @RequestMapping("/selectAi")
    public ResponseDTO selectAi(AiDifyVO aiDifyVO) {
        return ResponseDTO.success(aiService.selectAi(aiDifyVO));
    }

    // gljk 启用ai
    @RequestMapping("/enableAi")
    public ResponseDTO enableAi(Integer id) {
        Integer res = aiService.enableAi(id);
        if(res > 0){
            return ResponseDTO.success("操作成功");
        }else {
            return ResponseDTO.fail("操作失败");
        }
    }

    // gljk 删除ai
    @RequestMapping("/deleteAi")
    public ResponseDTO deleteAi(Integer id) {
        Integer res = aiService.deleteAi(id);
        if(res > 0){
            return ResponseDTO.success("删除成功");
        }else {
            return ResponseDTO.fail("删除失败");
        }
    }

    // gljk ai信息添加
    @RequestMapping("/insertAi")
    public ResponseDTO insertAi(AiDifyVO aiDifyVO) {
        Integer res = aiService.insertAi(aiDifyVO);
        if(res > 0){
            return ResponseDTO.success("添加成功");
        }else {
            return ResponseDTO.fail("添加失败");
        }
    }

    // gljk ai信息修改
    @RequestMapping("/updateAi")
    public ResponseDTO updateAi(AiDifyVO aiDifyVO) {
        Integer res = aiService.updateAi(aiDifyVO);
        if(res > 0){
            return ResponseDTO.success("修改成功");
        }else {
            return ResponseDTO.fail("修改失败");
        }
    }

}
