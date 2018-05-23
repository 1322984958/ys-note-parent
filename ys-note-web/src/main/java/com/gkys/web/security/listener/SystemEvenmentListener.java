package com.gkys.web.security.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gkys.web.security.filter.CustomDefaultFilterChainManager;
import com.gkys.web.security.filter.ShiroFilterChainManager;

public class SystemEvenmentListener implements ServletContextListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(SystemEvenmentListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		CustomDefaultFilterChainManager filterChainManager = (CustomDefaultFilterChainManager) applicationContext.getBean(CustomDefaultFilterChainManager.class);
		ShiroFilterChainManager shiroFilterChainManager = (ShiroFilterChainManager) applicationContext.getBean(ShiroFilterChainManager.class);
		filterChainManager.init();
		shiroFilterChainManager.init();
		shiroFilterChainManager.initFilterChain();
		LOGGER.info("init System Evenment.");
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
}