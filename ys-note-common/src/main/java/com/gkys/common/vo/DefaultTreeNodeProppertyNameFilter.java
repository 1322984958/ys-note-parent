package com.gkys.common.vo;

import java.util.Arrays;
import java.util.List;

public abstract class DefaultTreeNodeProppertyNameFilter extends TreeNodeProppertyNameFilter{
	private static List<String> treeNodeVoProppertyList=Arrays.asList("id","parentId","name","open","checked");

	@Override
	public String filterProperty(String propertyName) {
		if(treeNodeVoProppertyList.contains(propertyName)){
			return propertyName;
		}
		return "otherInfo";
	}

}
