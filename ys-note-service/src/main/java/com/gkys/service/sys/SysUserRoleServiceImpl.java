package com.gkys.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysUserRoleService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysUserRoleMapper;
import com.gkys.model.sys.SysUserRole;

@Service
@BaseService
@Transactional
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleMapper,SysUserRole,Long> implements SysUserRoleService{
}