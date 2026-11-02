package com.cykj.service.impl;

import com.cykj.enity.DO.EvaluationDO;
import com.cykj.enity.DO.QuestionDO;
import com.cykj.enity.vo.*;
import com.cykj.mapper.QuestionMapper;
import com.cykj.mapper.RiverMapper;
import com.cykj.mapper.SystemMapper;
import com.cykj.service.QuestionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private RiverMapper riverMapper;
    @Autowired
    private SystemMapper systemMapper;

    @Override
    public PageInfo<QuestionVO> selectQuestion(QuestionDO questionDO) {
        if(questionDO.getPageNum()==null && questionDO.getPageSize()==null){
            List<QuestionVO> questionVOS = questionMapper.selectQuestion(questionDO);
            PageInfo<QuestionVO> Info = new PageInfo<>(questionVOS);
            Info.setList(questionVOS);
            return Info;
        }
        PageHelper.startPage(questionDO.getPageNum(),questionDO.getPageSize());
        try {
            List<QuestionVO> questionVOS = questionMapper.selectQuestion(questionDO);
            return new PageInfo<>(questionVOS);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public Integer insertQuestion(QuestionVO questionVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        questionVO.setCreateTime(now);
        return questionMapper.insertQuestion(questionVO);
    }

    @Override
    public Integer updateQuestion(QuestionVO questionVO) {
        return questionMapper.updateQuestion(questionVO);
    }

    @Override
    public Integer deleteQuestion(Integer id) {
        return questionMapper.deleteQuestion(id);
    }

    @Override
    public PageInfo<EvaluationVO> selectEvaluation(EvaluationDO evaluationDO) {
        PageHelper.startPage(evaluationDO.getPageNum() !=null ? evaluationDO.getPageNum() : 1
                ,evaluationDO.getPageSize() !=null ? evaluationDO.getPageSize() : 10);
        try {
            List<EvaluationVO> evaluationVOS = questionMapper.selectEvaluation(evaluationDO);
            return new PageInfo<>(evaluationVOS);
        } finally {
            PageHelper.clearPage();
        }
    }

    @Override
    public EvaluationVO selectEvaluationById(Integer id) {
        return questionMapper.selectEvaluationById(id);
    }

    @Override
    public Integer insertEvaluation(EvaluationVO evaluationVO) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        evaluationVO.setCreateTime(now);
        MoneyFlowVO moneyFlowVO = new MoneyFlowVO();
        moneyFlowVO.setUserId(evaluationVO.getUserId());
        moneyFlowVO.setMoney(evaluationVO.getMoney());
        moneyFlowVO.setTip("答题咨询");
        moneyFlowVO.setCreateTime(now);
        riverMapper.insertRiver(moneyFlowVO);
        LogVO logVO = new LogVO();
        logVO.setType(4);
        logVO.setCreateTime(now);
        systemMapper.insertLog(logVO);
        return questionMapper.insertEvaluation(evaluationVO);
    }

    @Override
    public Integer answerReport(EvaluationReportVO evaluationReportVO) {
        Integer consultantId = evaluationReportVO.getConsultantId();
        questionMapper.updateEvaluationStatus(consultantId);
        return questionMapper.answerReport(evaluationReportVO);
    }

    @Override
    public Integer delEvaluationReport(Integer reportId) {
        return questionMapper.delEvaluationReport(reportId);
    }
}
