package com.cykj.enity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVO {
  private Integer roleId;
  private String name;
  private String desc;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;

  private Integer[] menuIds;
}
