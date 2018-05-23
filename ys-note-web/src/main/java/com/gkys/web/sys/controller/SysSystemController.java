package com.gkys.web.sys.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gkys.api.sys.SysSystemService;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.model.sys.SysSystem;
import com.gkys.model.sys.SysUser;

@Controller
@RestController
@RequestMapping(value = "api/system/")
public class SysSystemController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private SysSystemService sysSystemService;

	@PostMapping("list")
	public BaseResult list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer limit) {
		PageHelper.startPage(page, limit);
		List<SysSystem> list = sysSystemService.selectAll();
		Page<SysSystem> pageList = (Page<SysSystem>) list;
		return BaseResult.success(pageList);
	}
//
//	@PostMapping("save")
//	@FormToken(needRemoveToken = true)
//	public BaseResult save(SysRoleVo sysRoleVo) {
//		Syssystem sysRole=new SysRole();
//		BeanUtil.copyProperties(sysRoleVo, sysRole);
//		sysRoleService.save(sysRole);
//		return BaseResult.success();
//	}
//	
//	@DeleteMapping("del/{id}")
//	public BaseResult del(@PathVariable("id")Long id,String token) {
//		int i=sysRoleService.deleteByPrimaryKey(id);
//		return BaseResult.success();
//	}
}
