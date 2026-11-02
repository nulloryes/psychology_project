package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuVO extends MenuVO{
  private Integer divideId;
  private Integer menuId;
  private Integer roleId;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;

  private Integer[] menuIds;
  private List<MenuVO> menuList;
}
