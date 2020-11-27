package com.customerradar.user.controller;

import javax.validation.constraints.NotNull;

import com.customerradar.user.enums.StatusCode;
import com.customerradar.user.exception.CustomerRadarException;
import com.customerradar.user.service.IUserService;
import com.customerradar.user.vo.PageResult;
import com.customerradar.user.vo.Result;
import com.customerradar.user.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *  User Controller
 *  Manage User Interface
 *  Create User
 *  Update User
 *  Get User By Id
 *  Get User By Phone
 *  Delete User By Id
 */
@RestController
@ResponseBody
@RequestMapping("/api/user")
@CrossOrigin
public class UserController extends BaseController {
    
    @Autowired
    IUserService userService;

    /**
     * Get User By UserId
     * @param id
     * @return Result<UserVo> 
     */
    @GetMapping(value = "/{id}")
    public Result<UserVo> getUserById(@NotNull @PathVariable("id") Long id) {
        Result<UserVo> result = new Result<UserVo>();
        try {
            result.setStatusCode(StatusCode.SUCCESS.getCode());
            result.setData(userService.getUserById(id));

        } catch (CustomerRadarException ex) {
            result.setStatusCodeDes(ex.getStatusCode());
            logger.error("Get User By Id  = {}", id, ex);
        } catch (Exception ex) {
            result.setStatusCode(StatusCode.FAIL.getCode());
            logger.error("Get User By Id  = {}", id, ex);
        }
        return result;
    }

    /**
     * Get User By Phone
     * 
     * @param phone
     * @return
     */
    @GetMapping(value = "/phone/{phone}")
    public Result<UserVo> getUserByPhone(@NotNull @PathVariable("phone") String phone) {
        Result<UserVo> result = new Result<UserVo>();
        try {
            result.setStatusCode(StatusCode.SUCCESS.getCode());
            result.setData(userService.getUserByPhone(phone));

        } catch (CustomerRadarException ex) {
            result.setStatusCodeDes(ex.getStatusCode());
            logger.error("Get User By Phone  = {}", phone, ex);
        } catch (Exception ex) {
            result.setStatusCode(StatusCode.FAIL.getCode());
            logger.error("Get User By Phone  = {}", phone, ex);
        }
        return result;
    }

    @GetMapping(value = "/all/{pageIndex}/{pageSize}")
    public Result<PageResult<UserVo>> getUsers(@PathVariable("pageIndex") long pageIndex, @PathVariable("pageSize") long pageSize) {
        Result<PageResult<UserVo>> result = new Result<PageResult<UserVo>>();
        try {
            PageResult<UserVo> pageResult = userService.getUsers(pageIndex, pageSize);
            result.setData(pageResult);
            result.setStatusCodeDes(StatusCode.SUCCESS);

        } catch (CustomerRadarException ex) {
            result.setStatusCodeDes(ex.getStatusCode());
            logger.error("Get Users", ex);
        } catch (Exception ex) {
            result.setStatusCodeDes(StatusCode.FAIL);
            logger.error("Get Users", ex);
        }
        return result;
    }

    /**
     * Create User 
     * @param user
     * @return
     */
    @PutMapping(value = "/add")
    public Result<UserVo> createUser(@RequestBody @Validated UserVo user) {
        Result<UserVo> result = new Result<UserVo>();
        try {
            UserVo userVo = userService.createUser(user);
            result.setStatusCode(userVo != null ? StatusCode.SUCCESS.getCode() : StatusCode.FAIL.getCode());
            result.setData(userVo);
            
        } catch (CustomerRadarException ex) {
            result.setStatusCodeDes(ex.getStatusCode());
            logger.error("Create User", user, ex);
        } catch (Exception ex) {
            result.setStatusCode(StatusCode.FAIL.getCode());
            logger.error("Create User", user, ex);
        }
        return result;
    }

    /**
     * Update User By Id
     * @param user
     * @return
     */
    @PostMapping(value = "/update")
    public Result<UserVo> updateUser(@RequestBody @Validated UserVo user) {
        Result<UserVo> result = new Result<UserVo>();
        try {
            boolean flag = userService.updateUser(user);
            result.setStatusCode(flag?StatusCode.SUCCESS.getCode():StatusCode.FAIL.getCode());

        } catch (CustomerRadarException ex) {
            result.setStatusCodeDes(ex.getStatusCode());
            logger.error("Update User Fail ",ex);
        } catch (Exception ex) {
            result.setStatusCode(StatusCode.FAIL.getCode());
            logger.error("Update User Fail ", ex);
        }
        return result;
    }

    /**
     * Delete User By ID
     * @param id
     * @return
     */
    @DeleteMapping(value = "/del/{id}")
    public Result<UserVo> delUser(@NotNull @PathVariable("id") Long id) {
        Result<UserVo> result = new Result<UserVo>();
        try {
            boolean flag = userService.delUser(id);
            result.setStatusCode(flag?StatusCode.SUCCESS.getCode():StatusCode.FAIL.getCode());
        } catch (CustomerRadarException ex) {
            result.setStatusCodeDes(ex.getStatusCode());
            logger.error("Del User By Id  = {}", id, ex);
        } catch (Exception ex) {
            result.setStatusCode(StatusCode.FAIL.getCode());
            logger.error("Del User By Id  = {}", id, ex);
        }
        return result;
    }


}
