package com.gkys.web.security.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyRoleAuthorizationFilter extends RolesAuthorizationFilter {
	private static final Logger log = LoggerFactory.getLogger(AnyRoleAuthorizationFilter.class);

	private boolean isFilterChainContinued(ServletRequest request,ServletResponse response, String path, Object pathConfig)
			throws Exception {
		if (isEnabled(request, response, path, pathConfig)) {
			if (log.isTraceEnabled()) {
				log.trace("Filter '{}' is enabled for the current request under path '{}' with config [{}].  Delegating to subclass implementation for 'onPreHandle' check.",new Object[] { getName(), path, pathConfig });
			}

			return onPreHandle(request, response, pathConfig);
		}

		if (log.isTraceEnabled()) {
			log.trace("Filter '{}' is disabled for the current request under path '{}' with config [{}].  The next element in the FilterChain will be called immediately.",new Object[] { getName(), path, pathConfig });
		}

		return true;
	}

	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		if ((this.appliedPaths == null) || (this.appliedPaths.isEmpty())) {
			if (log.isTraceEnabled()) {
				log.trace("appliedPaths property is null or empty.  This Filter will passthrough immediately.");
			}
			return true;
		}
		List pathsList = new ArrayList();
		for (String path : this.appliedPaths.keySet()) {
			if (pathsMatch(path, request)) {
				log.trace("Current requestURI matches pattern '{}'.  Determining filter chain execution...",path);
				if (path.equals(getPathWithinApplication(request))) {
					Object config = this.appliedPaths.get(path);
					return isFilterChainContinued(request, response, path,config);
				}
				pathsList.add(path);
			}
		}

		if (pathsList.size() > 0) {
			Object config = this.appliedPaths.get(pathsList.get(0));
			return isFilterChainContinued(request, response,(String) pathsList.get(0), config);
		}

		return true;
	}

	public boolean isAccessAllowed(ServletRequest request,ServletResponse response, Object mappedValue) throws IOException {
		Subject subject = getSubject(request, response);

		String[] rolesArray = (String[]) mappedValue;
		if ((rolesArray == null) || (rolesArray.length == 0)) {
			return true;
		}

		Set<String> roles = CollectionUtils.asSet(rolesArray);
		for (String role : roles) {
			if (subject.hasRole(role)) {
				log.info("进入");
				return true;
			}
		}
		log.info("第二个进入出错了");
		return false;
	}
}