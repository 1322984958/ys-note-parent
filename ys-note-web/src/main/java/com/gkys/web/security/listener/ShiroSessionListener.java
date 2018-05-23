package com.gkys.web.security.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gkys.web.security.session.ShiroSessionDao;

public class ShiroSessionListener implements SessionListener {
	private static Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

	@Autowired
	private ShiroSessionDao shiroSessionDao;

	@Override
	public void onStart(Session session) {
		shiroSessionDao.create(session);
		logger.info("会话创建：" + session.getId());
	}

	@Override
	public void onStop(Session session) {
		shiroSessionDao.delete(session);
		logger.info("会话停止：" + session.getId());
	}

	@Override
	public void onExpiration(Session session) {
		shiroSessionDao.delete(session);
		logger.info("会话过期：" + session.getId());
	}

}
