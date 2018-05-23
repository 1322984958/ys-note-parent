package com.gkys.service.sys;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gkys.api.sys.SysOrganizationService;
import com.gkys.common.annotation.BaseService;
import com.gkys.common.base.BaseResult;
import com.gkys.common.base.BaseServiceImpl;
import com.gkys.mapper.sys.SysOrganizationMapper;
import com.gkys.model.sys.SysOrganization;

import tk.mybatis.mapper.entity.Example;

@Service
@BaseService
@Transactional
public class SysOrganizationServiceImpl extends BaseServiceImpl<SysOrganizationMapper,SysOrganization,Long> implements SysOrganizationService{

	@Override
	public BaseResult save(SysOrganization sysOrganization) {
		int result=0;
		//1、验证用户合法性
		if(sysOrganization.getId()==null){
			Example example=new Example(SysOrganization.class);
			example.createCriteria().andEqualTo("value", sysOrganization.getValue());
			int count=selectCountByExample(example);
			if(count>0){
				BaseResult baseResult=BaseResult.fail("有相同编码的部门");
				baseResult.setMessage("有相同编码的部门");
				baseResult.setCode(-2);
				return baseResult; //已存在
			}
			sysOrganization.setCreateTime(new Date());
			result=insertSelective(sysOrganization);
		}else{
			result=updateByPrimaryKey(sysOrganization);
		}
		BaseResult baseResult=BaseResult.success(sysOrganization);
		baseResult.setCode(result);
		return baseResult; //已存在
	}
}