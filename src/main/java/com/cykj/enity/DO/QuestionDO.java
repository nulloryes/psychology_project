package com.cykj.enity.DO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDO extends pageParam{
  private Integer questionId;
  private Integer domainId;
  private String question;
  private Integer status;
  private Timestamp createTime;
}
