package com.gkys.web.security.controller;

import static com.gkys.common.constant.BaseConstants.TOKEN_FORM;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gkys.api.sys.SysUserService;
import com.gkys.common.annotation.FormToken;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.common.captcha.CaptchaException;
import com.gkys.common.constant.BaseConstants;
import com.gkys.common.util.JwtsUtils;
import com.gkys.model.sys.SysUser;
import com.gkys.web.contant.ShiroContant;

@Controller
@RestController
@RequestMapping(value = "api/")
public class LoginController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private SysUserService sysUserService;

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(Model model) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated() || subject.isRemembered()) {
			model.addAttribute("account", subject.getPrincipal());
			return "redirect:index";
		}
		if (session.getAttribute("loginError") != null) {
			model.addAttribute("loginError", session.getAttribute("loginError"));
			session.removeAttribute("loginError");
		}
		return "login";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult doLogin(String username, String password, @RequestParam(defaultValue="false")Boolean rememberMe,Model model) {
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			return BaseResult.fail("用户名或密码不能为空");
		}
		String error = null;
		try {
			Subject subject = SecurityUtils.getSubject();
			if(subject.isRemembered()){
				SysUser record = new SysUser();
				record.setUsername((String)subject.getPrincipals().getPrimaryPrincipal());
				subject.login(new UsernamePasswordToken(record.getUsername(),"",true));
				final SysUser user = sysUserService.selectOne(record);
				session.setAttribute(ShiroContant.LOGIN_USER, user);
				return BaseResult.success().addData("user", user).addData(ShiroContant.LOGIN_TOKEN,
						JwtsUtils.createToken(String.valueOf(user.getId()),BaseConstants.JWT_DEFAULT_SECERT,DateUtils.addHours(new Date(),4)));
				
			}
			if (subject.isAuthenticated()) {
				SysUser user = (SysUser) session.getAttribute(ShiroContant.LOGIN_USER);
				return BaseResult.success().addData("user", user).addData(ShiroContant.LOGIN_TOKEN,
						JwtsUtils.createToken(String.valueOf(user.getId()),BaseConstants.JWT_DEFAULT_SECERT,DateUtils.addHours(new Date(),4)));
			}
			// 身份验证
			subject.login(new UsernamePasswordToken(username, password,rememberMe));
			// 验证成功在Session中保存用户信息
			SysUser record = new SysUser();
			record.setUsername(username);
			final SysUser sysUser = sysUserService.selectOne(record);
			session.setAttribute(ShiroContant.LOGIN_USER, sysUser);
		} catch (UnknownAccountException unknownAccountException) {
			error = "用户名有误";
		} catch (IncorrectCredentialsException unknownAccountException) {
			error = "用户名/密码错误";
		} catch (LockedAccountException lockedAccountException) {
			error = "当前账户已被锁定,请联系管理员";
		} catch (CaptchaException e) {
			error = "验证码错误";
		} catch (Exception e) {
			logger.error("登录出错：", e);
		}
		if (StringUtils.isNotBlank(error)) {
			return BaseResult.fail(error);
		}
		SysUser user = (SysUser) session.getAttribute(ShiroContant.LOGIN_USER);
		return BaseResult.success().addData("user", user).addData(ShiroContant.LOGIN_TOKEN,
				JwtsUtils.createToken(String.valueOf(user.getId()),BaseConstants.JWT_DEFAULT_SECERT,DateUtils.addHours(new Date(),4)));
//		return BaseResult.success().addData("user", user).addData(ShiroContant.LOGIN_TOKEN,
//				TokenUtil.createUserToken(user.getId()));
	}
	
	@RequestMapping(value = "loginOut", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult loginOut(String token) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		session.removeAttribute(ShiroContant.LOGIN_USER);
		return BaseResult.success();
	}

	@GetMapping("getToken")
	@ResponseBody
	public BaseResult getToken() {
		SysUser user = (SysUser) session.getAttribute(ShiroContant.LOGIN_USER);
		if(user==null){
			return BaseResult.fail("请登录");
		}
//		return BaseResult.success().addData(ShiroContant.LOGIN_TOKEN, TokenUtil.createUserToken(user.getId()));
		return BaseResult.success().addData(ShiroContant.LOGIN_TOKEN, JwtsUtils.createToken(String.valueOf(user.getId()),BaseConstants.JWT_DEFAULT_SECERT,DateUtils.addHours(new Date(),4)));
	}
	
	@GetMapping("getFormToken")
	@ResponseBody
	@FormToken(needAddToken = true)
	public BaseResult getFormToken() {
		Object token=request.getSession(false).getAttribute(BaseConstants.TOKEN_FORM);
		return BaseResult.success(token);
	}

}
