package com.gkys.web.security.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysUserService;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysUser;
import com.gkys.web.contant.ShiroContant;
import com.google.common.collect.Ordering;

@Controller
@RestController
@RequestMapping(value = "api/")
public class MenuController extends BaseController{
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	
	@RequiresRoles("superAdmin")
	@GetMapping(value = "menu")
	public BaseResult menu(String token,Model model) {
		SysUser sysUser=(SysUser)session.getAttribute(ShiroContant.LOGIN_USER);	
		Set<SysPermission> allSysPermissions=new HashSet<>();
		// 当前用户所有角色
		Set<SysRole> sysRoles = sysUserService.selectRolesByUserId(sysUser.getId());
		for (SysRole sysRole : sysRoles) {
			// 该角色下的权限
			if (StringUtils.isNotBlank(sysRole.getValue())) {
				Set<SysPermission> roleSysPermissions = sysRoleService.selectPermissionsByRoleId(sysRole.getId());
				allSysPermissions.addAll(roleSysPermissions);
			}
		}
		// 当前用户所有权限
		Set<SysPermission> sysPermissions = sysUserService.selectPermissionsByUserId(sysUser.getId());
		allSysPermissions.addAll(sysPermissions);
		Ordering<SysPermission> myOrdering = new Ordering<SysPermission>() {
            @Override
            public int compare(SysPermission a, SysPermission b) {
                return a.getOrders() - b.getOrders();
            }
        };
        List<SysPermission> sortedResult=myOrdering.sortedCopy(allSysPermissions);
        List<SysPermission> results = new ArrayList<SysPermission>();
        for(SysPermission one1 : sortedResult) {
			if(one1.getParentId()==null){
				List<SysPermission> subMenu = new ArrayList<SysPermission>();
				for(SysPermission one2 : sortedResult) {
					if(one1.getId().equals(one2.getParentId())) {
						subMenu.add(one2);
					}
				}
				one1.setChildrens(subMenu);
				results.add(one1);
			}
		}
		return BaseResult.success().addData("menus", results);
	}
}
