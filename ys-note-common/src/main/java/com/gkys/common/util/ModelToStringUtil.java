package com.gkys.common.util;

import java.lang.reflect.Field;

public class ModelToStringUtil {
	public static String toString(Class t,Object o) {
		StringBuffer sb = new StringBuffer();
		try {
			Field[] fields = t.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				sb.append("{");
				sb.append(field.getName());
				sb.append(":");
				if(field.get(o)==null){
					sb.append("null}");
					continue;
				}
				if (field.getType() == Integer.class) {
					sb.append(field.getInt(o));
				} else if (field.getType() == Long.class) {
					sb.append(field.getLong(o));
				} else if (field.getType() == Boolean.class) {
					sb.append(field.getBoolean(o));
				} else if (field.getType() == char.class) {
					sb.append(field.getChar(o));
				} else if (field.getType() == Double.class) {
					sb.append(field.getDouble(o));
				} else if (field.getType() == Float.class) {
					sb.append(field.getFloat(o));
				} else
					sb.append(field.get(o));
				sb.append("}");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
