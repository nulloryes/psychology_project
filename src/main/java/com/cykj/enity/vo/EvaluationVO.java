package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationVO extends UserVO{
  private Integer evaluationId;
  private Integer userId;
  private Integer domainId;
  private String tip;
  private double money;
  private Integer score;
  private Timestamp createTime;
  private Timestamp updateTime;
  private String domainName;
  private List<EvaluationReportVO> report;
}
