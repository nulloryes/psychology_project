package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
  private long userId;
  private String account;
  private String password;
  private String name;
  private String sex;
  private long age;
  private long phone;
  private String address;
  private long status;
  private long isDelete;
  private double money;
  private Timestamp createTime;
  private Timestamp updateTime;

}
