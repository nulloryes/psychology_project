package com.cykj.mapper;

import com.cykj.enity.DO.EvaluationDO;
import com.cykj.enity.DO.QuestionDO;
import com.cykj.enity.vo.EvaluationReportVO;
import com.cykj.enity.vo.EvaluationVO;
import com.cykj.enity.vo.QuestionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {
    List<QuestionVO> selectQuestion(@Param("questionDO") QuestionDO questionDO);

    Integer insertQuestion(@Param("questionVO") QuestionVO questionVO);

    Integer updateQuestion(@Param("questionVO") QuestionVO questionVO);

    Integer deleteQuestion(@Param("id") Integer id);

    List<EvaluationVO> selectEvaluation(@Param("evaluationDO") EvaluationDO evaluationDO);

    EvaluationVO selectEvaluationById(@Param("id") Integer id);

    Integer insertEvaluation(@Param("evaluationVO") EvaluationVO evaluationVO);

    Integer answerReport(@Param("evaluationReportVO") EvaluationReportVO evaluationReportVO);
    Integer updateEvaluationStatus(@Param("evaluationId") Integer evaluationId);

    Integer delEvaluationReport(@Param("reportId") Integer reportId);
}
