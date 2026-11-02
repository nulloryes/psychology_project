package com.cykj.enity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainDO extends pageParam{
  private Integer domainId;
  private String name;
  private Integer status;
}
