package com.gkys.web.security.filter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.apache.shiro.web.filter.mgt.SimpleNamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDefaultFilterChainManager extends DefaultFilterChainManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomDefaultFilterChainManager.class);

	private Map<String, String> filterChainDefinitionMap = null;
	private String loginUrl;
	private String successUrl;
	private String unauthorizedUrl;

	public CustomDefaultFilterChainManager() {
		setFilters(new LinkedHashMap());
		setFilterChains(new LinkedHashMap());
		addDefaultFilters(false);
	}

	public void setCustomFilters(Map<String, Filter> customFilters) {
		for (Map.Entry entry : customFilters.entrySet()) {
			addFilter((String) entry.getKey(), (Filter) entry.getValue(), false);
			LOGGER.info("filter name:{},value:{}", entry.getKey(), entry.getValue());
		}
	}

	public void setDefaultFilterChainDefinitions(String filterChainDefinitions) {
		LOGGER.info("filterChainDefinitions:{}", filterChainDefinitions);
		Ini ini = new Ini();
		ini.load(filterChainDefinitions);

		Ini.Section section = ini.getSection("urls");

		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection("");
		}
		setFilterChainDefinitionMap(section);
	}

	public void init() {
		Map<String,Filter> filters = getFilters();
		String name;
		if (!CollectionUtils.isEmpty(filters)) {
			for (Map.Entry entry : filters.entrySet()) {
				name = (String) entry.getKey();
				Filter filter = (Filter) entry.getValue();
				applyGlobalPropertiesIfNecessary(filter);
				if ((filter instanceof Nameable)) {
					((Nameable) filter).setName(name);
				}
			}

		}

		Map<String, String> chains = getFilterChainDefinitionMap();
		if (!CollectionUtils.isEmpty(chains))
			for (Map.Entry entry : chains.entrySet()) {
				String url = (String) entry.getKey();
				String chainDefinition = (String) entry.getValue();
				createChain(url, chainDefinition);
			}
	}

	protected void initFilter(Filter filter) {
	}

	public FilterChain proxy(FilterChain original, List<String> chainNames) {
		NamedFilterList configured = new SimpleNamedFilterList(chainNames.toString());
		for (String chainName : chainNames) {
			LOGGER.info("chain:{}", getChain(chainName));
			configured.addAll(getChain(chainName));
		}
		return configured.proxy(original);
	}

	private void applyGlobalPropertiesIfNecessary(Filter filter) {
		applyLoginUrlIfNecessary(filter);
		applySuccessUrlIfNecessary(filter);
		applyUnauthorizedUrlIfNecessary(filter);
	}

	private void applyLoginUrlIfNecessary(Filter filter) {
		String loginUrl = getLoginUrl();
		if ((StringUtils.hasText(loginUrl))
				&& ((filter instanceof AccessControlFilter))) {
			AccessControlFilter acFilter = (AccessControlFilter) filter;

			String existingLoginUrl = acFilter.getLoginUrl();
			if ("/login.jsp".equals(existingLoginUrl))
				acFilter.setLoginUrl(loginUrl);
		}
	}

	private void applySuccessUrlIfNecessary(Filter filter) {
		String successUrl = getSuccessUrl();
		if ((StringUtils.hasText(successUrl))
				&& ((filter instanceof AuthenticationFilter))) {
			AuthenticationFilter authcFilter = (AuthenticationFilter) filter;

			String existingSuccessUrl = authcFilter.getSuccessUrl();
			if ("/".equals(existingSuccessUrl))
				authcFilter.setSuccessUrl(successUrl);
		}
	}

	private void applyUnauthorizedUrlIfNecessary(Filter filter) {
		String unauthorizedUrl = getUnauthorizedUrl();
		if ((StringUtils.hasText(unauthorizedUrl)) && ((filter instanceof AuthorizationFilter))) {
			AuthorizationFilter authzFilter = (AuthorizationFilter) filter;

			String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
			if (existingUnauthorizedUrl == null)
				authzFilter.setUnauthorizedUrl(unauthorizedUrl);
		}
	}

	public Map<String, String> getFilterChainDefinitionMap() {
		return this.filterChainDefinitionMap;
	}

	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}

	public String getLoginUrl() {
		return this.loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getSuccessUrl() {
		return this.successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getUnauthorizedUrl() {
		return this.unauthorizedUrl;
	}

	public void setUnauthorizedUrl(String unauthorizedUrl) {
		this.unauthorizedUrl = unauthorizedUrl;
	}
}