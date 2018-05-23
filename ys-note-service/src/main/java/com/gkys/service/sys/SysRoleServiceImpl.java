package com.gkys.service.sys;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysSystemService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.common.exception.BaseException;
import com.gkys.mapper.sys.SysRoleMapper;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysSystem;

@Service
@BaseService
@Transactional
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper,SysRole,Long> implements SysRoleService{

	@Autowired
	private SysSystemService sysSystemService;
	
	@Override
	public Set<SysPermission> selectPermissionsByRoleId(Long roleId) {
		return mapper.selectPermissionsByRoleId(roleId);
	}

	@Override
	public int save(SysRole sysRole) {
		int result=0;
		//1、验证用户合法性
		if(sysRole.getId()==null){
			result=insertSelective(sysRole);
		}else{
			result=updateByPrimaryKey(sysRole);
		}
		return result;
	}

	@Override
	public SysSystem getSystemByRoleId(Long roleId) throws Exception {
		SysRole sysRole=mapper.selectByPrimaryKey(roleId);
		if(sysRole==null){
			throw new BaseException("不存在");
		}
		SysSystem sysSystem=sysSystemService.selectByPrimaryKey(sysRole.getSystemId());
		return sysSystem;
	}
}