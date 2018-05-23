package com.gkys.api.sys;

import java.util.List;
import java.util.Map;
import com.gkys.model.sys.SysPermission;
import com.gkys.common.base.BaseService;

public interface SysPermissionService extends BaseService<SysPermission,Long>{

	/**
	 * 保存角色下的权限
	 * @param roleId
	 * @param permIdList
	 * @return
	 */
	int saveRoleOfPermissions(Long roleId, List<String> permIdList);

	/**
	 * 新增或修改权限
	 * @param sysPermission
	 * @return
	 */
	int save(SysPermission sysPermission);
}