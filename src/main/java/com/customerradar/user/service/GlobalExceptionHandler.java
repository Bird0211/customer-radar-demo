package com.customerradar.user.service;

import com.customerradar.user.enums.StatusCode;
import com.customerradar.user.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    protected static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        
        Result result = new Result();
        result.setStatusCode(StatusCode.PARAM_ERROR.getCode());
        result.setDescription(e.getBindingResult().getFieldError().getDefaultMessage());
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(result);
            logger.info("Response Args  : {}", json);
        }catch (Exception ex) {
            logger.info("Response Args Error",ex);
        }
        return result;
    }

}
