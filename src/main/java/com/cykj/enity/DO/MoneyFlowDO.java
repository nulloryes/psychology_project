package com.cykj.enity.DO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyFlowDO extends pageParam{
  private Integer moneyFlowId;
  private String tip;
  private Integer userId;
  private String consultantName;
  private String consultantRole;
  private Integer status;
  private Integer consultantId;
  private String type;
  private double money;
}
