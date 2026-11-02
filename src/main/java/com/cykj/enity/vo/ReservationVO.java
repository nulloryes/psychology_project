package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationVO {

  private Integer reservationId;
  private Integer userId;
  private Integer domainId;
  private Integer consultantId;
  private Integer status;
  private double money;
  private String orderTime;
  private String problem;
  private String answer;
  private String rate;
  private Integer rateScore;
  private String rateTime;
  private Timestamp createTime;
  private Timestamp updateTime;

  private String userName;
  private String domainName;
  private String consultantName;
  private String role;
}
