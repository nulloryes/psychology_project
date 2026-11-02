package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuVO {
  private Integer menuId;
  private String name;
  private Integer upId;
  private Integer order;
  private String icon;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;
  private List<MenuVO> children = new ArrayList<>();
}
