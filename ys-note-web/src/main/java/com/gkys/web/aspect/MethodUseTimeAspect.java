package com.gkys.web.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gkys.common.base.BaseResult;

@Component
@Aspect
@Order(100) 
public class MethodUseTimeAspect {
	private static Logger logger = LoggerFactory.getLogger(MethodUseTimeAspect.class);
	 // 开始时间
    private long startTime = 0L;
    // 结束时间
    private long endTime = 0L;
    
	@Before("execution(* *..controller..*.*(..))")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
//        logger.debug("doBeforeInServiceLayer");
        startTime = System.currentTimeMillis();
    }
	
	@After("execution(* *..controller..*.*(..))")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
//        logger.debug("doAfterInServiceLayer");
    }
    @Around("execution(* *..controller..*.*(..))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
//        ParaMap paraMap = RequestUtil.getParameterMap(request);
//        logger.info("ip:{}",RequestUtil.getIpAddr(request));
        logger.info("Uri:{}",request.getRequestURI());
        logger.info("RequestURL:{}",ObjectUtils.toString(request.getRequestURL()));
        logger.info("User-Agent:{}",request.getHeader("User-Agent"));
        logger.info("method:{}",request.getMethod());
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (request.getMethod().equalsIgnoreCase("GET")) {
        	 logger.info(request.getQueryString());
        }else{
        	 logger.info(ObjectUtils.toString(request.getParameterMap()));
        }
        // 从注解中获取操作名称、获取响应结果
        Object result = new Object();
        try{
        	StopWatch clock = new StopWatch();
    		clock.start();
        	result=pjp.proceed();
        	clock.stop();
        	Class[] params = method.getParameterTypes();
    		String[] simpleParams = new String[params.length];
    		for (int i = 0; i < params.length; i++) {
    			simpleParams[i] = params[i].getSimpleName();
    		}
    		logger.debug("用时:" + clock.getTime() + " ms [" + method.getDeclaringClass().getName() + "." + method.getName() + "(" + StringUtils.join(simpleParams, ",") + ")] ");
        }catch(Exception e){
        	logger.error("出现异常：",e);
        	if(method.getReturnType() == BaseResult.class){
        		return BaseResult.fail("系统错误");
        	}else{
        		return "error/500.jsp";
        	}
        }
//        endTime = System.currentTimeMillis();
//        logger.debug("doAround>>>result={},耗时：{}", result, endTime - startTime);
//        logger.info("result:{}",ObjectUtils.toString(result));
//        logger.debug("结束时间：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
//                DateFormatUtils.format(new Date(endTime),"HH:mm:ss.SSS"),
//                (endTime - startTime),
//                request.getRequestURI(), Runtime.getRuntime().maxMemory() / 1024 / 1024,
//                Runtime.getRuntime().totalMemory() / 1024 / 1024,
//                Runtime.getRuntime().freeMemory() / 1024 / 1024,
//                (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory()) / 1024 / 1024);
        
        return result;
    }
}
