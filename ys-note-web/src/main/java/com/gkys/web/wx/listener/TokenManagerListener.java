package com.gkys.web.wx.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.gkys.common.util.PropertiesFileUtil;

import weixin.popular.client.LocalHttpClient;
import weixin.popular.support.TicketManager;
import weixin.popular.support.TokenManager;

public class TokenManagerListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//设置请求连接池
		LocalHttpClient.init(100,10);
		//请求超时 5秒
		LocalHttpClient.setTimeout(5000);
		//异常重试次数 2
		LocalHttpClient.setRetryExecutionCount(2);
		//设置请求连接池
		LocalHttpClient.init(100,10);
		//WEB容器 初始化时调用
		TokenManager.setDaemon(false);
		String appid=PropertiesFileUtil.getInstance("properties/application").get("wx.appid");
		String appsecret=PropertiesFileUtil.getInstance("properties/application").get("wx.appsecret");
		TokenManager.init(appid, appsecret);
		TicketManager.setDaemon(false);
		TicketManager.init(appid,60,60*119);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		//WEB容器  关闭时调用
		TokenManager.destroyed();
		TicketManager.destroyed();
	}

}
