package com.cykj.enity.vo;

import com.cykj.enity.vo.list.reservedVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingVO {
  private Integer adminId;
  private Integer schedulingId;
  private Integer consultantId;
  private String beginTime;
  private String endTime;
  private Integer status;
  private Timestamp createTime;
  private Timestamp updateTime;
  private List<reservedVO> reservedList;
}
