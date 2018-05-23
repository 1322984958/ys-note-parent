package com.gkys.web.security.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gkys.api.sys.SysOrganizationService;
import com.gkys.common.annotation.FormToken;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.common.util.BeanUtil;
import com.gkys.common.util.TreeNodeUtils;
import com.gkys.common.vo.TreeNodeVo;
import com.gkys.model.sys.SysOrganization;
import com.gkys.web.vo.sys.SysOrganizationVo;

import tk.mybatis.mapper.entity.Example;

@Controller
@RestController
@RequestMapping(value = "api/organization")
public class OrganizationController extends BaseController{
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	
	@Autowired
	private SysOrganizationService sysOrganizationService;
	
	@PostMapping("tree")
	public BaseResult tree(String token) {
		Example example=new Example(SysOrganization.class);
		example.setOrderByClause("orders asc,create_time desc");
		List<SysOrganization> sysOrganizationList=sysOrganizationService.selectByExample(example);
		List<TreeNodeVo> result=new ArrayList<>();
		try {
			result=new TreeNodeUtils().buildByRecursive(sysOrganizationList);
			for(TreeNodeVo treeNodeVo:result){
				treeNodeVo.setOpen(true);
			}
		} catch (Exception e) {
			logger.error("组装组织树出错：",e);
			return BaseResult.fail("系统错误!");
		}
		return BaseResult.success(result);
	}
	
	@PostMapping("save")
	@FormToken(needRemoveToken = true)
	public BaseResult save(SysOrganizationVo sysOrganizationVo){
		SysOrganization sysOrganization=new SysOrganization();
		BeanUtil.copyProperties(sysOrganizationVo, sysOrganization);
		return sysOrganizationService.save(sysOrganization);
	}
	
	@DeleteMapping("delete/{id}")
	public BaseResult delete(@PathVariable("id")Long id,String token) {
		SysOrganization sysOrganization=new SysOrganization();
		sysOrganization.setParentId(id);
		int num=sysOrganizationService.selectCount(sysOrganization);
		if(num>0){
			BaseResult baseResult=BaseResult.fail("该部门下还有其他关联部门，不能删除");
			baseResult.setMessage("该部门下还有其他关联部门，不能删除");
			return baseResult;
		}
		int i=sysOrganizationService.deleteByPrimaryKey(id);
		return BaseResult.success();
	}
}
