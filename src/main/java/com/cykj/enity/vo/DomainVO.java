package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainVO {
  private Integer domainId;
  private String name;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;

}
