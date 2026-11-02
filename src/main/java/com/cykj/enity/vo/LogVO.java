package com.cykj.enity.vo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogVO {
  private Integer logId;
  private Integer type;
  private String remark;
  private Timestamp createTime;

  private String startDate;
  private String endDate;
}
