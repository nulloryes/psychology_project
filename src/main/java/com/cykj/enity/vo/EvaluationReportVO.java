package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationReportVO{
  private Integer reportId;
  private String result;
  private Integer consultantId;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;
  private String consultantName;
  private String role;
}
