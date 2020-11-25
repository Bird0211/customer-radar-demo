package com.customerradar.user.service;

import com.customerradar.user.exception.CustomerRadarException;
import com.customerradar.user.vo.UserVo;

/**
 * User Service
 */
public interface IUserService {
    
    UserVo getUserByPhone(String phone) throws CustomerRadarException;

    UserVo createUser(UserVo user) throws CustomerRadarException;

    boolean updateUser(UserVo user) throws CustomerRadarException;

    boolean delUser(Long id) throws CustomerRadarException;

    UserVo getUserById(Long userId) throws CustomerRadarException;

}
