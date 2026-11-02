package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyFlowVO extends ConsultantVO{
  private Integer moneyFlowId;
  private String tip;
  private Integer userId;
  private Integer consultantId;
  private Integer status;
  private String type;
  private double money;
  private Timestamp createTime;
}
