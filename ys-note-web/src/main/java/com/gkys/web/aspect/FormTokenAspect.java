package com.gkys.web.aspect;

import static com.gkys.common.constant.BaseConstants.TOKEN_FORM;

import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.gkys.common.annotation.FormToken;
import com.gkys.common.exception.RepeatedSubmitFormException;

@Component
@Aspect
@Order(1)
public class FormTokenAspect {
	private Logger logger = LoggerFactory.getLogger(FormTokenAspect.class);

	@Pointcut(value = "execution(* com.gkys.web..*Controller*.insert*(..))")
	public void pointcut1() {
	}

	@Pointcut(value = "execution(* com.gkys.web..*Controller*.update*(..))")
	public void pointcut2() {
	}

	@Pointcut(value = "execution(* com.gkys.web..*Controller*.save*(..))")
	public void pointcut3() {
	}

	@Pointcut(value = "execution(* com.gkys.web..LoginController.getFormToken*(..))")
	public void pointcut4() {
	}

	// @Before("@args(com.gkys.common.annotation.FormToken)")
	@Before(value = "pointcut1()||pointcut2()||pointcut3()||pointcut4()")
	public void before(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		Object target = joinPoint.getTarget();
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		FormToken formToken = method.getAnnotation(FormToken.class);
		if (formToken == null) {
			logger.debug("前置增强 为空");
			return;
		}

		if (formToken.needAddToken()) {
			request.getSession(false).setAttribute(TOKEN_FORM, UUID.randomUUID().toString());
		}

		if (formToken.needRemoveToken()) {
			if (isRepeatSubmit(request)) {
				// 不允许重复提交
				throw new RepeatedSubmitFormException();
			}
			// 移除session中保存的旧token
			request.getSession(false).removeAttribute(TOKEN_FORM);
		}
	}

	private boolean isRepeatSubmit(HttpServletRequest request) {
		String serverToken = (String) request.getSession(false).getAttribute(TOKEN_FORM);
		if (serverToken == null) {
			return true;
		}
		String clientToken = request.getParameter(TOKEN_FORM);

		if (StringUtils.isEmpty(clientToken)) {
			return true;
		}
		if (!serverToken.equals(clientToken)) {
			return true;
		}
		// log.debug("校验是否重复提交：表单页面Token值为："+clientToken +
		// ",Session中的Token值为:"+serverToken);
		return false;
	}
}
