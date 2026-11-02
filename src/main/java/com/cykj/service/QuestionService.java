package com.cykj.service;

import com.cykj.enity.DO.EvaluationDO;
import com.cykj.enity.DO.QuestionDO;
import com.cykj.enity.vo.EvaluationReportVO;
import com.cykj.enity.vo.EvaluationVO;
import com.cykj.enity.vo.QuestionVO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

public interface QuestionService {
    PageInfo<QuestionVO> selectQuestion(QuestionDO questionDO);

    Integer insertQuestion(QuestionVO questionVO);

    Integer updateQuestion(QuestionVO questionVO);

    Integer deleteQuestion(Integer id);

    PageInfo<EvaluationVO> selectEvaluation(EvaluationDO evaluationDO);

    EvaluationVO selectEvaluationById(Integer id);

    Integer insertEvaluation(EvaluationVO evaluationVO);

    Integer answerReport(EvaluationReportVO evaluationReportVO);

    Integer delEvaluationReport(Integer reportId);
}
