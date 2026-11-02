package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO{
  private Integer userId;
  private String account;
  private String password;
  private String name;
  private String sex;
  private Integer age;
  private String avatar;
  private String phone;
  private String address;
  private Integer status;
  private Integer isDelete;
  private double money;
  private Timestamp createTime;
  private Timestamp updateTime;

  private String code;
  private String unique;
}
