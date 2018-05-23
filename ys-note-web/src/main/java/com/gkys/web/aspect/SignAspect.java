package com.gkys.web.aspect;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.gkys.common.base.BaseResult;
import com.gkys.common.constant.BaseConstants;
import com.gkys.common.token.TokenCheckResult;
import com.gkys.common.util.JwtsUtils;

import io.jsonwebtoken.Claims;

@Aspect
@Component
@Order(2)
public class SignAspect {
	private static Logger logger = LoggerFactory.getLogger(SignAspect.class);
	
//	@Around("@within(org.springframework.web.bind.annotation.RequestMapping)")
	@Around("execution(* *..controller..*.*(..))")
    public Object signCheck(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		String uri=request.getRequestURI();
		logger.debug("进入sign验证,uri:{}",uri);
		if(uri.contains("/api/login")||!uri.contains("/api")){
			return pjp.proceed();
		}
		
		String tokenStr = request.getParameter("token");
		if (tokenStr == null || tokenStr.equals("")) {
			PrintWriter printWriter = response.getWriter();
			printWriter.print(JSONObject.toJSON(BaseResult.fail("请求不合法!")));
			printWriter.flush();
			printWriter.close();
			return null;
		}
		
		// 验证JWT的签名，返回CheckResult对象
		TokenCheckResult tokenCheckResult = JwtsUtils.parseTokenGetResult(tokenStr, BaseConstants.JWT_DEFAULT_SECERT);
		if (tokenCheckResult.isSuccess()) {
			Claims claims = tokenCheckResult.getClaims();
			logger.debug(">>>>>获得的claims：{}",claims.toString());
				return pjp.proceed();
//			SubjectModel model = GsonUtil.jsonStrToObject(claims.getSubject(), SubjectModel.class);
//			request.setAttribute("tokensub", model);
//			request.getRequestDispatcher("/success.jsp").forward(request, response);
		} else {
			switch (tokenCheckResult.getErrCode()) {
				// 签名过期，返回过期提示码
				case BaseConstants.JWT_ERRCODE_EXPIRE:
					PrintWriter printWriter = response.getWriter();
					printWriter.print(JSONObject.toJSON(BaseResult.fail("签名过期!")));
					printWriter.flush();
					printWriter.close();
					break;
				// 签名验证不通过
				case BaseConstants.JWT_ERRCODE_FAIL:
					PrintWriter printWriter2 = response.getWriter();
					printWriter2.print(JSONObject.toJSON(BaseResult.fail("签名错误!")));
					printWriter2.flush();
					printWriter2.close();
					break;
				default:
					break;
			}
			return null;
		}
	}
}
