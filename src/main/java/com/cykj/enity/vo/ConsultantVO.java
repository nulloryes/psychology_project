package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantVO extends AdminVO{
  private Integer consultantId;
  private String school;
  private String role;
  private String project;
  private String profile;
  private Integer rateScore;
  private double price;
  private Integer adminId;
  private String headPicture;
  private Timestamp createTime;
  private Timestamp updateTime;
  private List<DomainVO> domain;
  private Integer[] domains;
}
