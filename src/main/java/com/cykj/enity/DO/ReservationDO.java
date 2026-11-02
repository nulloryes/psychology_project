package com.cykj.enity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDO extends pageParam{
  private Integer userId;
  private Integer domainId;
  private Integer consultantId;
  private Integer adminId;
  private String userName;
  private String domainName;
  private String consultantName;
  private Integer status;
  private String orderTime;
  private String problem;

  private String startDate;
  private String endDate;
}
