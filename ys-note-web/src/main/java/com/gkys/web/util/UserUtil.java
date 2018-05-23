package com.gkys.web.util;

import javax.servlet.http.HttpSession;

import com.gkys.model.sys.SysUser;
import com.gkys.web.contant.ShiroContant;

public class UserUtil {
	/**
     * 设置用户到session
     * @param session
     * @param user
     */
    public static void saveUserToSession(HttpSession session, SysUser user) {
    	session.setAttribute(ShiroContant.LOGIN_USER, user);
    }

    /**
     * 从Session获取当前用户信息
     *
     * @param session
     * @return
     */
    public static SysUser getUserFromSession(HttpSession session) {
        Object attribute = session.getAttribute(ShiroContant.LOGIN_USER);
        return attribute == null ? null : (SysUser) attribute;
    }
}
