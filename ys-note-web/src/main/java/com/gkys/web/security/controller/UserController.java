package com.gkys.web.security.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gkys.api.sys.SysSystemService;
import com.gkys.api.sys.SysUserService;
import com.gkys.common.annotation.FormToken;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.common.util.BeanUtil;
import com.gkys.common.util.PropertiesFileUtil;
import com.gkys.model.sys.SysSystem;
import com.gkys.model.sys.SysUser;
import com.gkys.web.vo.security.SysUserVo;

@Controller
@RestController
@RequestMapping(value = "api/user")
public class UserController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysSystemService sysSystemService;

	@PostMapping("list")
	public BaseResult list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer limit) {
		PageHelper.startPage(page, limit);
		List<SysUser> list = sysUserService.selectAll();
		Page<SysUser> pageList = (Page<SysUser>) list;
		return BaseResult.success(pageList);
	}

	@PostMapping("save")
	@FormToken(needRemoveToken = true)
	public BaseResult save(SysUserVo sysUserVo) {
		SysUser sysUser=new SysUser();
		if(StringUtils.isNotBlank(sysUserVo.getPassword())){
			String rememberMeName=PropertiesFileUtil.getInstance("properties/application").get("rememberMe.id");
			Cookie cookie = new Cookie(rememberMeName,null);
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		}
		BeanUtil.copyProperties(sysUserVo, sysUser);
		SysSystem record=new SysSystem();
		record.setCategory(sysUserVo.getSysSystemVo().getCategory());
		record=sysSystemService.selectOne(record);
		if(record==null){
			return BaseResult.fail("数据有误");
		}
		sysUser.setSystemId(record.getId());
		sysUserService.save(sysUser);
		return BaseResult.success();
	}
	
	@PostMapping("assignRoles")
	public BaseResult assignRoles(@RequestParam("roleIds[]")List<Long> roleIds,@RequestParam("userIds[]")List<Long> userIds) {
		sysUserService.assignRoles(roleIds,userIds);
		return BaseResult.success();
	}
}
