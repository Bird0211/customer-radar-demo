package com.customerradar.user.vo;

import javax.validation.constraints.NotNull;

import com.customerradar.user.JsonLongSerializer;
import com.customerradar.user.po.User;
import com.customerradar.user.service.PhoneNumber;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class UserVo {
    
    public UserVo () {

    }

    public UserVo(User user) {
        if(user != null) {
            setId(user.getId());
            setName(user.getName());
            setAddress(user.getAddress());
            setPhone(user.getPhone());
        }
    }

    @JsonSerialize(using = JsonLongSerializer.class )
    Long id;

    @NotNull(message = "name is null")
    String name;

    String address;

    @PhoneNumber(message = "Invalid phone number")
    @NotNull(message = "phone number is null")
    String phone;

    @JsonIgnore
    public boolean isEmpty() {
        if(this.id == null || this.id <= 0)
            return true;
        
        if(StringUtils.isEmpty(this.name))
            return true;
        
        if(StringUtils.isEmpty(this.phone))
            return true;
        
        return false;
    }

}
