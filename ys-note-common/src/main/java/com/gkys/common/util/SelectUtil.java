package com.gkys.common.util;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import tk.mybatis.mapper.entity.Example;

public class SelectUtil {
	public final static String LIKE = "like";
	public final static String EQUAL = "equal";

	public static void addParameterIfNotNull(Example.Criteria criteria, String type, String property,
			String parameter) {
		if (StringUtils.isNotBlank(parameter)) {
			if (type.equals(LIKE)) {
				criteria.andLike(property, "%" + parameter + "%");
			} else if (type.equals(EQUAL)) {
				criteria.andEqualTo(property, parameter);
			}
		}
	}

	public static void addParameterBetweenIfNotNull(Example.Criteria criteria, String property, String startTime,
			String endTime) {
		if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
			criteria.andGreaterThan(property, startTime);
		} else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			criteria.andLessThan(property, endTime);
		} else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			criteria.andBetween(property, startTime, endTime);
		}
	}
	
	/**
	 * 包含EqualTo
	 * @param criteria
	 * @param property
	 * @param startTime
	 * @param endTime
	 */
	public static void addParameterEqualToBetweenIfNotNull(Example.Criteria criteria, String property, String startTime,
			String endTime) {
		if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)) {
			criteria.andGreaterThanOrEqualTo(property, startTime);
		} else if (StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			criteria.andLessThanOrEqualTo(property, endTime);
		} else if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			criteria.andBetween(property, startTime, endTime);
		}
	}

	public static void addParameterIfNotNull(Map<String, Object> para, String property, String parameter) {
		if (StringUtils.isNotBlank(parameter)) {
			para.put(property, parameter);
		}
	}
}
