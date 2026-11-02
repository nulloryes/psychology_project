package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminVO{
  private Integer adminId;
  private String account;
  private String password;
  private String name;
  private Integer status;
  private double money;
  private Integer isDelete;
  private String roleName;
  private Integer roleId;
  private String headPicture;
  private Timestamp createTime;
  private Timestamp updateTime;

  private String code;
  private String unique;
  private String oldPassword;
}
