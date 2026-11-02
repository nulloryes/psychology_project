package com.cykj.enity.DO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDO extends pageParam{
  private Integer userId;
  private Integer consultantId;
  private Integer domainId;
  private String userName;
  private Integer status;
  private String tip;
  private Timestamp createTime;
}
