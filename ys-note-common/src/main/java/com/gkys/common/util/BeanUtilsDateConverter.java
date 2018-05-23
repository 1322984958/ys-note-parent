package com.gkys.common.util;

import java.text.ParseException;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.time.DateUtils;

public class BeanUtilsDateConverter implements Converter {

	@SuppressWarnings({"unchecked"})
	@Override
	public <T> T convert(Class<T> myClass, Object myObj) {
		if (myObj==null) {
			return null;
		}
		Object convertObj=myObj;
		if (myObj instanceof Object[]) {
			convertObj=((Object[])myObj)[0];
		}
		if (convertObj instanceof String) {
			Object convertValue=null;
			try {
				convertValue = DateUtils.parseDate(convertObj.toString(), new String[] {"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyy-MM-dd HH:mm","yyyy年MM月dd日"});
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return (T)convertValue;
		}else {
			return (T)convertObj;
		}
	}
/*

		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return (T) df.parse(myObj.toString());
		} catch (ParseException e) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				return (T) df.parse(myObj.toString());
			} catch (ParseException e1) {
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
					return (T) df.parse(myObj.toString());
				} catch (ParseException e2) {
					e2.printStackTrace();
				}
			}
		}
		return null;
	}*/
}