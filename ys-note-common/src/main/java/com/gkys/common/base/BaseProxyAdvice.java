package com.gkys.common.base;

import java.lang.reflect.Method;

public interface BaseProxyAdvice {
	public void beforeMethod(Method method);
	public void afterMethod(Method method);
}
