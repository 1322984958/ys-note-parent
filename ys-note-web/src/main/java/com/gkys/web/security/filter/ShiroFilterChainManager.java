package com.gkys.web.security.filter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.NamedFilterList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//import com.ysweb.infra.security.model.UrlAccessResource;
//import com.ysweb.infra.security.service.SecurityAccessService;

@Component
public class ShiroFilterChainManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroFilterChainManager.class);

	@Autowired
	private DefaultFilterChainManager filterChainManager;

//	@Autowired
//	private SecurityAccessService securityAccessService;
	private Map<String, NamedFilterList> defaultFilterChains;

	public void init() {
		this.defaultFilterChains = new LinkedHashMap(this.filterChainManager.getFilterChains());
		LOGGER.info("defaultFilterChains:{}", this.defaultFilterChains);
	}

	public void initFilterChain() {
//		List results = this.securityAccessService.findAllUrlAccessResources();
//		LOGGER.info("initFilterChain:{}", results);
//		initFilterChains(results);
	}

//	public void initFilterChains(List<UrlAccessResource> urlAccessResources) {
//		this.filterChainManager.getFilterChains().clear();
//		if (this.defaultFilterChains != null) {
//			this.filterChainManager.getFilterChains().putAll(this.defaultFilterChains);
//		}
//
//		if (!urlAccessResources.isEmpty()) {
//			for (UrlAccessResource urlAuthority : urlAccessResources) {
//				String url = urlAuthority.getUrl();
//				LOGGER.info("roles:{},permissions:{}", urlAuthority.getRoles(),urlAuthority.getPermissions());
//				if (StringUtils.hasText(url)) {
//					if (!urlAuthority.getRoles().isEmpty()) {
//						listToString(urlAuthority.getRoles());
//						LOGGER.info("url:{}----rol：{}", url,listToString(urlAuthority.getRoles()));
//						this.filterChainManager.addToChain(url, "anyRole",listToString(urlAuthority.getRoles()));
//					}
//					if (!urlAuthority.getPermissions().isEmpty()) {
//						this.filterChainManager.addToChain(url, "perms",listToString(urlAuthority.getPermissions()));
//					}
//				}
//			}
//		}
//		LOGGER.info("filterChain:{}", this.filterChainManager.getFilterChains());
//	}

	private String listToString(Collection<String> elements) {
		StringBuilder allRoles = new StringBuilder();
		for (String element : elements) {
			allRoles.append(element).append(",");
		}
		return allRoles.substring(0, allRoles.length() - 1);
	}
}