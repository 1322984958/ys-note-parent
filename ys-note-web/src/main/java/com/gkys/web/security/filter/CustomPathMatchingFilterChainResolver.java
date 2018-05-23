package com.gkys.web.security.filter;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomPathMatchingFilterChainResolver extends PathMatchingFilterChainResolver {
	private static final Logger logger = LoggerFactory.getLogger(CustomPathMatchingFilterChainResolver.class);
	private CustomDefaultFilterChainManager customDefaultFilterChainManager;

	public void setCustomDefaultFilterChainManager(CustomDefaultFilterChainManager customDefaultFilterChainManager) {
		this.customDefaultFilterChainManager = customDefaultFilterChainManager;
		setFilterChainManager(customDefaultFilterChainManager);
	}

	public FilterChain getChain(ServletRequest request,ServletResponse response, FilterChain originalChain) {
		HttpServletRequest httpRequest = (HttpServletRequest)request;  
		FilterChainManager filterChainManager = getFilterChainManager();
		if (!filterChainManager.hasChains()) {
			return null;
		}

		String requestURI = getPathWithinApplication(request);
		List chainNames = new ArrayList();
		for (String pathPattern : filterChainManager.getChainNames()) {
			if (pathMatches(pathPattern, requestURI)) {
				if (pathPattern.equals(requestURI)) {
					NamedFilterList b = this.customDefaultFilterChainManager.getChain(pathPattern);
					return this.customDefaultFilterChainManager.proxy(originalChain, pathPattern);
				}
				chainNames.add(pathPattern);
			}
		}

		if (chainNames.size() > 0) {
			return this.customDefaultFilterChainManager.proxy(originalChain,(String) chainNames.get(0));
		}
		return null;
	}
}