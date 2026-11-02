package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDifyVO {
  private Integer aiId;
  private String difyNumber;
  private String difyLocal;
  private String remarks;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;
}
