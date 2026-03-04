package com.cykj.enity.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class pageParam {
    private int pageNum;
    private int pageSize;
}
