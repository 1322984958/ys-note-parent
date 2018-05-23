package com.gkys.web.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gkys.common.captcha.Captcha;
import com.gkys.common.captcha.GifCaptcha;

public class CaptchaServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String codeKey = request.getParameter("codeKey");
		if ((codeKey == null) || (codeKey.trim().isEmpty())) {
			return;
		}

		response.setContentType("image/gif");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);

		Captcha captcha = new GifCaptcha(130, 38, 5);

		ServletContext servletContext = request.getSession().getServletContext();
		servletContext.setAttribute("code_" + codeKey, captcha.text().toLowerCase());

		captcha.out(response.getOutputStream());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
