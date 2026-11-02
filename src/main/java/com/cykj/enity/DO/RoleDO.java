package com.cykj.enity.DO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDO extends pageParam{
  private Integer roleId;
  private String name;
  private Integer status;
}
