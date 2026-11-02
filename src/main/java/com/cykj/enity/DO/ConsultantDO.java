package com.cykj.enity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultantDO extends pageParam{
    private Integer adminId;
    private String name;
    private String role;
    private double price;
    private Integer domainId;
    private Integer status;
}
