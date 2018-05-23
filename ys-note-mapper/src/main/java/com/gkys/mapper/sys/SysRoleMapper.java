package com.gkys.mapper.sys;

import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.gkys.common.base.BaseMapper;

public interface SysRoleMapper extends BaseMapper<SysRole,Long>{

	/**
	 * 查询角色对应的权限
	 * @param roleId
	 * @return
	 */
	Set<SysPermission> selectPermissionsByRoleId(@Param("roleId")Long roleId);
}