package com.gkys.common.vo;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class TreeNodeProppertyNameFilter{
	
	/**
	 * 得到需要转换的类型
	 * @author Administrator
	 */
	public abstract Class transformationClassName();
	/**
	 * 返回转换后对应的属性名
	 * @return
	 */
	public abstract String filterProperty(String propertyName);
	
	/**
	 * 得到所有的属性名,TreeNode的属性对应的会复制，不对应的会放在otherInfo，重写此方法可禁用不需要的属性复制
	 * @return
	 */
	public List<String> getPropertyNames(){
		List<String> propertyNames=new ArrayList<>();
		Class clazz=transformationClassName();
		PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);
		for(PropertyDescriptor propertyDescriptor:propertyDescriptors){
			propertyNames.add(propertyDescriptor.getName());
		}
		return propertyNames;
	}
}
