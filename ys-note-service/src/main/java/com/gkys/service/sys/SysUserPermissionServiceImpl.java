package com.gkys.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysUserPermissionService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysUserPermissionMapper;
import com.gkys.model.sys.SysUserPermission;

@Service
@BaseService
@Transactional
public class SysUserPermissionServiceImpl extends BaseServiceImpl<SysUserPermissionMapper,SysUserPermission,Long> implements SysUserPermissionService{
}