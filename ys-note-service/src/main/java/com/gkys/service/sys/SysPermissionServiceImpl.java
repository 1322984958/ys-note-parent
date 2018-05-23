package com.gkys.service.sys;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysPermissionService;
import com.gkys.api.sys.SysRolePermissionService;
import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysUserService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysPermissionMapper;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysRolePermission;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

@Service
@BaseService
@Transactional
public class SysPermissionServiceImpl extends BaseServiceImpl<SysPermissionMapper, SysPermission, Long>
		implements SysPermissionService {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRolePermissionService sysRolePermissionService;
	
	@Override
	public int saveRoleOfPermissions(Long roleId, List<String> permIdList) {
		int result=0;
		SysRole sysRole=sysRoleService.selectByPrimaryKey(roleId);
		if(sysRole==null){
			return result; 
		}
		//1、查询数据库中角色下所有权限。和这次上传的权限相比，如果上传的权限ids没有则删除
		Set<SysPermission> sysPermissionSet=sysRoleService.selectPermissionsByRoleId(roleId);
		for(SysPermission sysPermission:sysPermissionSet){
			Boolean exist=permIdList.contains(String.valueOf(sysPermission.getId()));
			if(!exist){//不包含，则需要从数据库中删除
				SysRolePermission sysRolePermission=new SysRolePermission(roleId,sysPermission.getId());
				sysRolePermissionService.delete(sysRolePermission);
			}
		}
		//2、查询数据库中角色下所有权限。和这次上传的权限相比，如果数据库中没有则增加
		Set<SysPermission> existSysPermissionSet=sysRoleService.selectPermissionsByRoleId(roleId);
		for(final String permId:permIdList){
			Set<SysPermission> selectResult=Sets.filter(existSysPermissionSet, new Predicate<SysPermission>() {
				@Override
				public boolean apply(SysPermission arg0) {
					if(String.valueOf(arg0.getId()).equals(permId)){
						return true;
					}
					return false;
				}
			});
			Boolean exist=CollectionUtils.isEmpty(selectResult);
			if(exist){//数据库中不存在，保存
				SysRolePermission sysRolePermission=new SysRolePermission(roleId,Long.parseLong(permId));
				sysRolePermissionService.insertSelective(sysRolePermission);
			}
		}
		return result;
	}

	@Override
	public int save(SysPermission sysPermission) {
		int result=0;
		//1、验证用户合法性
		if(sysPermission.getId()==null){
			sysPermission.setCreateTime(new Date());
			result=insertSelective(sysPermission);
		}else{
			result=updateByPrimaryKey(sysPermission);
		}
		return 0;
	}
}