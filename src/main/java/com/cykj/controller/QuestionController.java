package com.cykj.controller;

import com.cykj.enity.DO.EvaluationDO;
import com.cykj.enity.DO.QuestionDO;
import com.cykj.enity.dto.ResponseDTO;
import com.cykj.enity.vo.EvaluationReportVO;
import com.cykj.enity.vo.EvaluationVO;
import com.cykj.enity.vo.QuestionVO;
import com.cykj.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("question")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    //yhjk gljk查看题库（根据题类型查询）
    @RequestMapping("/selectQuestion")
    public ResponseDTO selectQuestion(QuestionDO questionDO) {
        return ResponseDTO.success(questionService.selectQuestion(questionDO));
    }
    //gljk 增加题型
    @RequestMapping("/insertQuestion")
    public ResponseDTO insertQuestion(QuestionVO questionVO) {
        questionService.insertQuestion(questionVO);
        return ResponseDTO.success("新增成功");
    }
    //gljk 修改题型
    @RequestMapping("/updateQuestion")
    public ResponseDTO updateQuestion(QuestionVO questionVO) {
        questionService.updateQuestion(questionVO);
        return ResponseDTO.success("修改成功");
    }
    //gljk 删除题型
    @RequestMapping("/deleteQuestion")
    public ResponseDTO deleteQuestion(Integer id) {
        questionService.deleteQuestion(id);
        return ResponseDTO.success("删除成功");
    }

    //yhjk 生成问卷报告(给出报告结果)
    @RequestMapping("/insertMyReport")
    public ResponseDTO insertMyReport(EvaluationVO evaluationReportVO) {
        questionService.insertEvaluation(evaluationReportVO);
        return ResponseDTO.success("报告已生成");
    }
    //yhjk gljk 查看问卷报告(传userId就是查自己的报告)
    @RequestMapping("/selectMyReport")
    public ResponseDTO selectReport(EvaluationDO evaluationDO) {
        return ResponseDTO.success(questionService.selectEvaluation(evaluationDO));
    }

    //yhjk gljk 查看报告具体内容
    @RequestMapping("/selectReportById")
    public ResponseDTO selectReportById(Integer id) {
        return ResponseDTO.success(questionService.selectEvaluationById(id));
    }

    //gljk 给问卷报告专家答复
    @RequestMapping("/answerReport")
    public ResponseDTO answerReport(EvaluationReportVO evaluationReportVO) {
        return ResponseDTO.success(questionService.answerReport(evaluationReportVO));
    }
    //gljk 删除报告
    @RequestMapping("/deleteReportType")
    public ResponseDTO deleteReportType(Integer reportId) {
        return ResponseDTO.success(questionService.delEvaluationReport(reportId));
    }
}
