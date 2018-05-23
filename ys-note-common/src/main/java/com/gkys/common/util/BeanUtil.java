package com.gkys.common.util;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanUtil {
	static {
		// 支持属性空值复制
		BeanUtilsBean.getInstance().getConvertUtils().register(false, true, 0);
		// 日期类型复制
		BeanUtilsDateConverter converter = new BeanUtilsDateConverter();
		ConvertUtils.register(converter, java.util.Date.class);
		ConvertUtils.register(converter, java.sql.Date.class);
	}
	/**
	 * Java对象之间属性值拷贝
	 * 
	 * @param pFromObj
	 *            Bean源对象
	 * @param pToObj
	 *            Bean目标对象
	 */
	public static void copyProperties(Object pFromObj, Object pToObj) {
		if (pToObj != null) {
			try {
				BeanUtils.copyProperties(pToObj, pFromObj);
			} catch (Exception e) {
				throw new RuntimeException("JavaBean之间的属性值拷贝发生错误", e);
			}
		}
	}

	public static void copyPropertiesIgnoreNull(Object src, Object target){
		if (target != null && src !=null) {
			java.lang.reflect.Field[] fields = target.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				try {
					String name = fields[i].getName();
					if (PropertyUtils.isReadable(src, name) && PropertyUtils.isWriteable(src, name)) {
						if(PropertyUtils.getProperty(src,name)!=null){
							BeanUtils.copyProperty(target, name,PropertyUtils.getProperty(src,name));
						}
					}
				}catch (Exception e){
					throw new RuntimeException("JavaBean之间的属性值拷贝发生错误", e);
				}
			}
		}
	}
	
	public static Map<String, Object> getParamMap(HttpServletRequest request) {
		Map<String,Object> reqMap = new HashMap<String, Object>();
		Map<String, String[]> map = request.getParameterMap();
		Iterator<String> keyIterator = (Iterator) map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			String value = map.get(key)[0];
			reqMap.put(key, value);
		}
		return reqMap;
	}
}
