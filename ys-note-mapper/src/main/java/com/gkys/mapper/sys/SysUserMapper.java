package com.gkys.mapper.sys;

import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.gkys.common.base.BaseMapper;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser,Long>{

	/**
	 * 获得用户角色
	 * @param userId
	 * @return
	 */
	Set<SysRole> selectRolesByUserId(@Param("userId")Long userId);

	/**
	 * 获得用户权限
	 * @param userId
	 * @return
	 */
	Set<SysPermission> selectPermissionsByUserId(@Param("userId")Long userId);
}