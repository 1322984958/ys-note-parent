package com.gkys.common.base;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.gkys.common.exception.RepeatedSubmitFormException;

public abstract class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 基于@ExceptionHandler异常处理 .
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = {RepeatedSubmitFormException.class})
	public BaseResult repeatedSubmitFormhandle(HttpServletRequest request,HttpServletResponse response, RepeatedSubmitFormException ex) {
		BaseResult baseResult=BaseResult.fail("提交太频繁了，请稍后重试");
		baseResult.setMessage("提交太频繁了，请稍后重试");
		return baseResult;
	}
	/**
	 * 基于@ExceptionHandler异常处理 .
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(value = {Exception.class})
	public void handle(HttpServletRequest request,HttpServletResponse response, Exception ex) {
		//打印异常堆栈
		logger.error("控制器出错：",ex);
		request.setAttribute("exception", ex.getMessage());
		if(request.getRequestURI().contains("api/")){
			response.setCharacterEncoding("UTF-8");    
			response.setContentType("application/json; charset=utf-8");
			try {
				response.getWriter().write(JSONObject.toJSONString(BaseResult.fail("操作失败")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// 根据不同错误转向不同页面
		try {
			response.sendRedirect("error/500.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前请求的token
	 */
	public String getToken(HttpServletRequest request) {
		String token = request.getHeader("token");
		if (token == null) {
			token = request.getParameter("token");
		}
		return token;
	}
}
