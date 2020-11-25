package com.customerradar.user.aop;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(public * com.customerradar.user.controller.*.*(..))")
    public void log(){}

    ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        startTime.set(System.currentTimeMillis());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // Start log
        logger.info("========================================== Start ==========================================");
        // Request url
        logger.info("URL            : {}", request.getRequestURL().toString());
        // Http method
        logger.info("HTTP Method    : {}", request.getMethod());
        // Controller Path and Method
        logger.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        // Request IP
        logger.info("IP             : {}", request.getRemoteAddr());
        // Request Args
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(joinPoint.getArgs());
            logger.info("Request Args   : {}", json);
        }catch (Exception e) {
            logger.info("Request Args Error",e);
        }


    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(result);
            logger.info("Response Args  : {}", json);
        }catch (Exception e) {
            logger.info("Response Args Error",e);
        }
        // sprend time
        logger.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    /**
     * 
     * @throws Throwable
     */
    @After("log()")
    public void doAfter() throws Throwable {
        logger.info("=========================================== End ===========================================");
        // 每个请求之间空一行
        logger.info("");
    }

}
