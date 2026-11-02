package com.cykj.enity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDO extends pageParam{
  private String account;
  private String name;
  private Integer status;
  private Integer roleId;
  
}
