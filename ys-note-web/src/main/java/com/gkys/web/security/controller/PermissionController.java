package com.gkys.web.security.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gkys.api.sys.SysPermissionService;
import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysSystemService;
import com.gkys.common.base.BaseController;
import com.gkys.common.base.BaseResult;
import com.gkys.common.util.BeanUtil;
import com.gkys.common.util.TreeNodeUtils;
import com.gkys.common.vo.DefaultTreeNodeProppertyNameFilter;
import com.gkys.common.vo.TreeNodeVo;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysSystem;
import com.gkys.web.vo.security.SysPermissionVo;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import tk.mybatis.mapper.entity.Example;

@Controller
@RestController
@RequestMapping(value = "api/permission/")
public class PermissionController extends BaseController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private HttpSession session;
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysPermissionService sysPermissionService;
	@Autowired
	private SysSystemService sysSystemService;
	
	
	@PostMapping("tree/{id}")
	public BaseResult tree(@PathVariable("id")Long id,String token) {
		SysSystem sysSystem=null;
		try {
			sysSystem = sysRoleService.getSystemByRoleId(id);
		} catch (Exception e1) {
			return BaseResult.fail("数据有误！");
		}
		Set<SysPermission> roleOfPermissionSet=sysRoleService.selectPermissionsByRoleId(id);
		Example example=new Example(SysPermission.class);
		example.setOrderByClause("create_time desc");
		example.createCriteria().andEqualTo("systemId", sysSystem.getId());
		List<SysPermission> sysPermissionList=sysPermissionService.selectByExample(example);
		//如果已经分配，则增加checked
		for(final SysPermission p:roleOfPermissionSet){
			Predicate<SysPermission> predicate = new Predicate<SysPermission>() {  
		        @Override  
		        public boolean apply(SysPermission input) {  
		            return input.getId().equals(p.getId());  
		        }  
		    };
		    Collection<SysPermission> selectSysPermissionList=Collections2.filter(sysPermissionList, predicate);
		    for(SysPermission sysPermission:selectSysPermissionList){
		    	sysPermission.setChecked(true);
		    }
		}
		List<TreeNodeVo> result=new ArrayList<>();
		try {
			result=new TreeNodeUtils().buildByRecursive(sysPermissionList);
		} catch (Exception e) {
			logger.error("组装权限树出错：",e);
			return BaseResult.fail("系统错误!");
		}
		return BaseResult.success(result);
	}
	
	@PostMapping("list")
	public BaseResult list(){
		List<SysPermission> sysPermissionList=sysPermissionService.selectAll();
		List<TreeNodeVo> result=new ArrayList<>();
		try {
			for(SysPermission sysPermission:sysPermissionList){
				DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter = new DefaultTreeNodeProppertyNameFilter() {
					@Override
					public Class transformationClassName() {
						return SysPermission.class;
					}
				};
				TreeNodeVo treeNodeVo=new TreeNodeUtils().assembleTreeNodeVo(defaultTreeNodeProppertyNameFilter, sysPermission);
				if(treeNodeVo.getParentId()==null){
					treeNodeVo.setParentId(0L);
				}
				result.add(treeNodeVo);
			}
		} catch (Exception e) {
			logger.error("组装权限树出错：",e);
			return BaseResult.fail("系统错误!");
		} 
//		List<TreeNodeVo> result=new ArrayList<>();
//			result=new TreeNodeUtils().buildByRecursive(sysPermissionList);
//			for(TreeNodeVo treeNodeVo:result){
//				if(treeNodeVo.getParentId()==null){
//					treeNodeVo.setParentId("0");
//				}
//			}
//		} catch (Exception e) {
//			logger.error("组装权限树出错：",e);
//			return BaseResult.fail("系统错误!");
//		}
		return BaseResult.success(result);
	}
	
	/**
	 * 保存权限
	 * @param roleId
	 * @param permIdList
	 * @param token
	 * @return
	 */
	@PostMapping("save")
	public BaseResult save(SysPermissionVo sysPermissionVo,String token) {
		SysPermission sysPermission=new SysPermission();
		BeanUtil.copyProperties(sysPermissionVo, sysPermission);
		SysSystem record=new SysSystem();
		record.setCategory(sysPermissionVo.getSysSystemVo().getCategory());
		record=sysSystemService.selectOne(record);
		if(record==null){
			return BaseResult.fail("数据有误");
		}
		sysPermission.setSystemId(record.getId());
		sysPermissionService.save(sysPermission);
		return BaseResult.success();
	}
	
	@PostMapping("tree/saveRolePermissions")
	public BaseResult saveRolePermissions(Long roleId,@RequestParam(value = "permIds[]")List<String> permIdList,String token) {
		sysPermissionService.saveRoleOfPermissions(roleId,permIdList);
		return BaseResult.success();
	}
	
	@PostMapping("tree/saveUserPermissions")
	public BaseResult saveUserPermissions(Long roleId,@RequestParam(value = "permIds[]")List<String> permIdList,String token) {
//		sysPermissionService.saveUserOfPermissions(roleId,permIdList);
		return BaseResult.success();
	}
	
	@PostMapping("parent/{category}")
	public BaseResult parent(@PathVariable(value = "category")String category,String token){
		SysPermission sysPermission=new SysPermission();
		sysPermission.setCategory(category);
		List<SysPermission> sysPermissionList=sysPermissionService.select(sysPermission);
		return BaseResult.success(sysPermissionList);
	}
}
