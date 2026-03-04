package com.cykj.enity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> {
    private int pageNum;
    private int pageSize;
    private int total;
    private List<T> list;
}
