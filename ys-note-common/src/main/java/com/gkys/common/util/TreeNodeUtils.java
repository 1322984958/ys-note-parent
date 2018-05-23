package com.gkys.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.gkys.common.vo.DefaultTreeNodeProppertyNameFilter;
import com.gkys.common.vo.TreeNodeProppertyNameFilter;
import com.gkys.common.vo.TreeNodeVo;

public class TreeNodeUtils<T> {
	public List<TreeNodeVo> buildByRecursive(List<T> list,DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter) throws Exception {
		return buildByRecursive(list, "parentId",defaultTreeNodeProppertyNameFilter);
	}

	/**
	 * 使用递归方法建树
	 * 
	 * @param treeNodes
	 * @return
	 * @throws Exception
	 */
	public List<TreeNodeVo> buildByRecursive(List<T> list, String parentIdPropertyName,DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter) throws Exception {
		List<TreeNodeVo> trees = new ArrayList<TreeNodeVo>();
		for (final T t : list) {
			Object parentId = PropertyUtils.getProperty(t, parentIdPropertyName);
			if (parentId == null || "0".equals(parentId)) {
				TreeNodeVo treeNodeVo = assembleTreeNodeVo(defaultTreeNodeProppertyNameFilter, t);
				// 转换treeNodeVo
				trees.add(findChildren(treeNodeVo, parentIdPropertyName, list));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 * 
	 * @param treeNodes
	 * @return
	 */
	public TreeNodeVo findChildren(TreeNodeVo treeNodeVo, String parentIdPropertyName, List<T> treeNodes,DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter)
			throws Exception {
		for (final T it : treeNodes) {
			Object parentId = PropertyUtils.getProperty(it, parentIdPropertyName);
			if (treeNodeVo.getId().equals(parentId)) {
				if (treeNodeVo.getChildrens() == null) {
					treeNodeVo.setChildrens(new ArrayList<TreeNodeVo>());
				}
				TreeNodeVo itTreeNodeVo=assembleTreeNodeVo(defaultTreeNodeProppertyNameFilter, it);
				treeNodeVo.getChildrens().add(findChildren(itTreeNodeVo, parentIdPropertyName, treeNodes));
			}
		}
		return treeNodeVo;
	}
	
	
	public List<TreeNodeVo> buildByRecursive(List<T> list) throws Exception {
		return buildByRecursive(list, "parentId");
	}

	/**
	 * 使用递归方法建树
	 * 
	 * @param treeNodes
	 * @return
	 * @throws Exception
	 */
	public List<TreeNodeVo> buildByRecursive(List<T> list, String parentIdPropertyName) throws Exception {
		List<TreeNodeVo> trees = new ArrayList<TreeNodeVo>();
		for (final T t : list) {
			DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter = new DefaultTreeNodeProppertyNameFilter() {
				@Override
				public Class transformationClassName() {
					return t.getClass();
				}
			};
			Object parentId = PropertyUtils.getProperty(t, parentIdPropertyName);
			if (parentId == null || "0".equals(parentId)) {
				TreeNodeVo treeNodeVo = assembleTreeNodeVo(defaultTreeNodeProppertyNameFilter, t);
				// 转换treeNodeVo
				trees.add(findChildren(treeNodeVo, parentIdPropertyName, list));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 * 
	 * @param treeNodes
	 * @return
	 */
	public TreeNodeVo findChildren(TreeNodeVo treeNodeVo, String parentIdPropertyName, List<T> treeNodes)
			throws Exception {
		for (final T it : treeNodes) {
			DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter = new DefaultTreeNodeProppertyNameFilter() {
				@Override
				public Class transformationClassName() {
					return it.getClass();
				}
			};
			Object parentId = PropertyUtils.getProperty(it, parentIdPropertyName);
			if (treeNodeVo.getId().equals(parentId)) {
				if (treeNodeVo.getChildrens() == null) {
					treeNodeVo.setChildrens(new ArrayList<TreeNodeVo>());
				}
				TreeNodeVo itTreeNodeVo=assembleTreeNodeVo(defaultTreeNodeProppertyNameFilter, it);
				treeNodeVo.getChildrens().add(findChildren(itTreeNodeVo, parentIdPropertyName, treeNodes));
			}
		}
		return treeNodeVo;
	}

	/**
	 * 递归查找子节点
	 * 
	 * @param treeNodes
	 * @return
	 */
	public TreeNodeVo assembleTreeNodeVo(DefaultTreeNodeProppertyNameFilter defaultTreeNodeProppertyNameFilter,T it) throws Exception {
		TreeNodeVo treeNodeVo = new TreeNodeVo();
		List<String> propertyNames = defaultTreeNodeProppertyNameFilter.getPropertyNames();
		for (String propertyName : propertyNames) {
			if("class".equals(propertyName)){
				continue;
			}
			String treeNodeVoPropertyName = defaultTreeNodeProppertyNameFilter.filterProperty(propertyName);
			Object value = PropertyUtils.getProperty(it, propertyName);
			if ("otherInfo".equals(treeNodeVoPropertyName)) {
				if (MapUtils.isEmpty(treeNodeVo.getOtherInfo())) {
					treeNodeVo.setOtherInfo(new HashMap<>());
				}
				treeNodeVo.getOtherInfo().put(propertyName, value);
			} else {
				PropertyUtils.setProperty(treeNodeVo, treeNodeVoPropertyName, value);
			}
		}
		return treeNodeVo;
	}
}
