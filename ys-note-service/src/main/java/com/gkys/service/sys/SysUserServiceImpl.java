package com.gkys.service.sys;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysUserRoleService;
import com.gkys.api.sys.SysUserService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.annotation.MethodParaValidate;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.common.service.EncryptService;
import com.gkys.common.util.CodeUtils;
import com.gkys.common.util.SelectUtil;
import com.gkys.mapper.sys.SysUserMapper;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysUser;
import com.gkys.model.sys.SysUserRole;

import tk.mybatis.mapper.entity.Example;

@Service
@BaseService
@Transactional
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper,SysUser,Long> implements SysUserService{
	@Autowired
	private EncryptService encryptService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;
	
	@Override
	public Set<SysRole> selectRolesByUserId(Long userId) {
		return mapper.selectRolesByUserId(userId);
	}

	@Override
	public Set<SysPermission> selectPermissionsByUserId(Long userId) {
		return mapper.selectPermissionsByUserId(userId);
	}

	@Override
	public SysUser getUserByUsername(String username) {
		Example example=new Example(SysUser.class);
		Example.Criteria criteria = example.createCriteria();
		SelectUtil.addParameterIfNotNull(criteria, SelectUtil.EQUAL, "username", username);
		SysUser sysUser=getOneByExample(example);
		return sysUser;
	}

	@Override
	public int save(@MethodParaValidate SysUser sysUser) {
		int result=0;
		//1、验证用户合法性
		if(StringUtils.isBlank(sysUser.getSalt())){
			sysUser.setSalt(CodeUtils.getUUID());
		}
		if(StringUtils.isNotBlank(sysUser.getPassword())){
			sysUser.setPassword(encryptService.encryptPassword(sysUser.getUsername()+sysUser.getPassword(), sysUser.getSalt()));
		}
		if(sysUser.getId()==null){
			SysUser record=new SysUser();
			record.setUsername(sysUser.getUsername());
			int count=mapper.selectCount(record);
			if(count>0){
				return -1;//已存在该用户名
			}
			sysUser.setCreateTime(new Date());
			result=insertSelective(sysUser);
		}else{
			sysUser.setLastModifyTime(new Date());
			result=updateByPrimaryKey(sysUser);
		}
		return result;
	}

	@Override
	public int assignRoles(List<Long> roleIds, List<Long> userIds) {
		if(CollectionUtils.isEmpty(userIds)||CollectionUtils.isEmpty(roleIds)){
			return 0;
		}
		for(Long userId:userIds){
			SysUser sysUser=mapper.selectByPrimaryKey(userId);
			if(sysUser==null){
				continue;
			}
			for(Long roleId:roleIds){
				SysRole sysRole=sysRoleService.selectByPrimaryKey(roleId);
				if(sysRole==null){
					continue;
				}
				userAssignRole(sysUser, sysRole);
			}
		}
		return 0;
	}
	
	private void userAssignRole(SysUser sysUser,SysRole sysRole){
		SysUserRole record=new SysUserRole();
		record.setUserId(sysUser.getId());
		record.setRoleId(sysRole.getId());
		SysUserRole sysUserRole=sysUserRoleService.selectOne(record);
		if(sysUserRole==null){
			sysUserRoleService.insertSelective(record);
		}
	}
	
}