package com.gkys.web.security.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysSystemService;
import com.gkys.common.annotation.FormToken;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.common.util.BeanUtil;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysSystem;
import com.gkys.web.vo.security.SysRoleVo;

@Controller
@RestController
@RequestMapping(value = "api/role/")
public class RoleController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysSystemService sysSystemService;

	@PostMapping("list")
	public BaseResult list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "20") Integer limit) {
		PageHelper.startPage(page, limit);
		List<SysRole> list = sysRoleService.selectAll();
		Page<SysRole> pageList = (Page<SysRole>) list;
		return BaseResult.success(pageList);
	}

	@PostMapping("save")
	@FormToken(needRemoveToken = true)
	public BaseResult save(SysRoleVo sysRoleVo) {
		SysRole sysRole=new SysRole();
		BeanUtil.copyProperties(sysRoleVo, sysRole);
		SysSystem record=new SysSystem();
		record.setCategory(sysRoleVo.getSysSystemVo().getCategory());
		record=sysSystemService.selectOne(record);
		sysRole.setSystemId(record.getId());
		sysRoleService.save(sysRole);
		return BaseResult.success();
	}
	
	@DeleteMapping("del/{id}")
	public BaseResult del(@PathVariable("id")Long id,String token) {
		int i=sysRoleService.deleteByPrimaryKey(id);
		return BaseResult.success();
	}
}
