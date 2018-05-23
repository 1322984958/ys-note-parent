package com.gkys.api.sys;

import java.util.List;
import java.util.Set;

import com.gkys.common.annotation.MethodParaValidate;
import com.gkys.common.base.BaseService;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysUser;

public interface SysUserService extends BaseService<SysUser,Long>{

	/**
	 * 获得用户角色
	 * @param id
	 * @return
	 */
	Set<SysRole> selectRolesByUserId(Long userId);

	/**
	 * 获得用户权限
	 * @param userId
	 * @return
	 */
	Set<SysPermission> selectPermissionsByUserId(Long userId);

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	SysUser getUserByUsername(String username);

	/**
	 * 插入或更新用户
	 * @param sysUser
	 * @return
	 */
	int save(@MethodParaValidate SysUser sysUser);

	/**
	 * 分配角色
	 * @param roleIds
	 * @param userIds
	 * @return
	 */
	int assignRoles(List<Long> roleIds, List<Long> userIds);
}