package com.gkys.api.sys;

import java.util.Set;

import com.gkys.common.base.BaseService;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysSystem;

public interface SysRoleService extends BaseService<SysRole,Long>{

	/**
	 * 查询角色对应的权限
	 * @param roleId
	 * @return
	 */
	Set<SysPermission> selectPermissionsByRoleId(Long roleId);

	/**
	 * 保存role
	 * @param sysRole
	 * @return
	 */
	int save(SysRole sysRole);
	
	/**
	 * 根据角色得到是属于哪个系统，如果不存在抛出异常
	 * @param roleId
	 * @return
	 */
	SysSystem getSystemByRoleId(Long roleId) throws Exception;
}