package com.gkys.web.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gkys.common.exception.ForbiddenIpException;
import com.gkys.common.util.PropertiesFileUtil;

/**
 * ip拦截前置拦截器
 */
@Aspect
@Component
@Order(1) // order值越小越先执行
public class ForbiddenIpAspect {
	private static Logger logger = LoggerFactory.getLogger(ForbiddenIpAspect.class);
	
//	@Autowired
//    private SystemService systemService;

    @Before("@within(org.springframework.web.bind.annotation.RequestMapping)")
    public void forbiddenIp() throws ForbiddenIpException {
        if (PropertiesFileUtil.getInstance("properties/application").getBool("forbiddenIp")) {
        	logger.debug("打开 ip intercepter : {}", true);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String remoteAddr = request.getRemoteAddr();
//            if (systemService.isForbiddenIp(remoteAddr)) {
//            	logger.error(" {}  ip 被禁用", remoteAddr);
//                throw new ForbiddenIpException(remoteAddr);
//            }
        }
    }
}
