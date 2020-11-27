package com.customerradar.user.vo;

import java.util.List;

import lombok.Data;

@Data
public class PageResult<T> {

    private long pageIndex;
    
    private long pageSize;

    private long total;

    private List<T> data;

}
