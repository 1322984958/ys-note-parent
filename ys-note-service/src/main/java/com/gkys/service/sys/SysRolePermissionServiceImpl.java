package com.gkys.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysRolePermissionService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysRolePermissionMapper;
import com.gkys.model.sys.SysRolePermission;

@Service
@BaseService
@Transactional
public class SysRolePermissionServiceImpl extends BaseServiceImpl<SysRolePermissionMapper,SysRolePermission,Long> implements SysRolePermissionService{
}