package com.customerradar.user.vo;

import com.customerradar.user.enums.StatusCode;

import lombok.Data;

/**
 * Result 
 */
@Data
public class Result<T> {
    
    private String description;
    private int statusCode;
    private T data;

    public void setStatusCodeDes(StatusCode statusCode) {
        this.setStatusCode(statusCode.getCode());
        this.setDescription(statusCode.getCodeMsg());
    }

}
