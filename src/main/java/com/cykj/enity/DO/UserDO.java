package com.cykj.enity.DO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDO extends pageParam{
  private String account;
  private String name;
  private String phone;
  private Integer status;
}
