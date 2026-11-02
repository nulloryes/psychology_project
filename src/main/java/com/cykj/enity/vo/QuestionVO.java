package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVO {
  private Integer questionId;
  private Integer domainId;
  private String question;
  private String aq;
  private Integer as;
  private String bq;
  private Integer bs;
  private String cq;
  private Integer cs;
  private String dq;
  private Integer ds;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;

}
