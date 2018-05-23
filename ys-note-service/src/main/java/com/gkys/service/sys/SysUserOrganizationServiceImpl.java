package com.gkys.service.sys;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysUserOrganizationService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysUserOrganizationMapper;
import com.gkys.model.sys.SysUserOrganization;

@Service
@BaseService
@Transactional
public class SysUserOrganizationServiceImpl extends BaseServiceImpl<SysUserOrganizationMapper,SysUserOrganization,Long> implements SysUserOrganizationService{
}