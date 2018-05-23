package com.gkys.web.security.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gkys.common.util.RedisUtil;
import com.gkys.common.util.SerializableUtil;
import com.gkys.model.sys.SysUser;
import com.gkys.web.contant.ShiroContant;

import redis.clients.jedis.Jedis;

public class ShiroSessionDao extends CachingSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(ShiroSessionDao.class);
    // 会话key
    private final static String YS_SHIRO_SESSION_ID = "ys-shiro-session-id";
    // 全局会话列表key
    private final static String YS_SERVER_SESSION_IDS = "ys-server-session-ids";
    // code key
    private final static String YS_SERVER_CODE = "ys-server-code";

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        RedisUtil.set(YS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        logger.debug("doCreate >>>>> sessionId={}", sessionId);
        RedisUtil.lpush(YS_SERVER_SESSION_IDS,sessionId.toString());
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String session = RedisUtil.get(YS_SHIRO_SESSION_ID + "_" + sessionId);
        logger.debug("doReadSession >>>>> sessionId={}", sessionId);
        return SerializableUtil.deserialize(session);
    }

    @Override
    protected void doUpdate(Session session) {
        // 如果会话过期/停止 没必要再更新了
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return;
        }
        // 更新session的最后一次访问时间
        ShiroSession shiroSession = (ShiroSession) session;
        ShiroSession cacheShiroSession = (ShiroSession) doReadSession(session.getId());
        if (null != cacheShiroSession) {
            shiroSession.setStatus(cacheShiroSession.getStatus());
            shiroSession.setAttribute("FORCEloggerOUT", cacheShiroSession.getAttribute("FORCEloggerOUT"));
        }
        RedisUtil.set(YS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
        // 更新YS_SERVER_SESSION_ID、YS_SERVER_CODE过期时间 TODO
        logger.debug("doUpdate >>>>> sessionId={} ,过期时间={}", session.getId(),session.getTimeout() / 1000);
    }

    @Override
    protected void doDelete(Session session) {
        String sessionId = session.getId().toString();
        // 删除session
        RedisUtil.remove(YS_SHIRO_SESSION_ID + "_" + sessionId);
        // 维护会话id列表，提供会话分页管理
        RedisUtil.lrem(YS_SERVER_SESSION_IDS, 1, sessionId);
        logger.debug("Session {} 被删除", session.getId());
    }

    /**
     * 获取会话列表
     * @param offset
     * @param limit
     * @return
     */
    public Map getActiveSessions(int offset, int limit) {
        Map sessions = new HashMap();
        Jedis jedis = RedisUtil.getJedis();
        // 获取在线会话总数
        long total = jedis.llen(YS_SERVER_SESSION_IDS);
        // 获取当前页会话详情
        List<String> ids = jedis.lrange(YS_SERVER_SESSION_IDS, offset, (offset + limit - 1));
        List<Session> rows = new ArrayList<>();
        for (String id : ids) {
            String session = RedisUtil.get(YS_SHIRO_SESSION_ID + "_" + id);
            // 过滤redis过期session
            if (null == session) {
                RedisUtil.lrem(YS_SERVER_SESSION_IDS, 1, id);
                total = total - 1;
                continue;
            }else{
                SysUser sysUser=(SysUser)SerializableUtil.deserialize(session).getAttribute(ShiroContant.LOGIN_USER);
                if(sysUser==null){
                    total = total - 1;
                    continue;
                }
            }
             rows.add(SerializableUtil.deserialize(session));
        }
        jedis.close();
        sessions.put("total", total);
        sessions.put("rows", rows);
        return sessions;
    }

    /**
     * 强制退出
     * @param ids
     * @return
     */
    public int forceout(String ids) {
        String[] sessionIds = ids.split(",");
        for (String sessionId : sessionIds) {
            // 会话增加强制退出属性标识，当此会话访问系统时，判断有该标识，则退出登录
            String session = RedisUtil.get(YS_SHIRO_SESSION_ID + "_" + sessionId);
            ShiroSession shiroSession = (ShiroSession) SerializableUtil.deserialize(session);
            shiroSession.setStatus(ShiroSession.OnlineStatus.force_logout);
            shiroSession.setAttribute("FORCEloggerOUT", ShiroSession.OnlineStatus.force_logout);
            RedisUtil.set(YS_SHIRO_SESSION_ID + "_" + sessionId, SerializableUtil.serialize(shiroSession), (int) shiroSession.getTimeout() / 1000);
        }
        return sessionIds.length;
    }

    /**
     * 更改在线状态
     *
     * @param sessionId
     * @param onlineStatus
     */
    public void updateStatus(Serializable sessionId, ShiroSession.OnlineStatus onlineStatus) {
        ShiroSession session = (ShiroSession) doReadSession(sessionId);
        if (null == session) {
            return;
        }
        session.setStatus(onlineStatus);
        RedisUtil.set(YS_SHIRO_SESSION_ID + "_" + session.getId(), SerializableUtil.serialize(session), (int) session.getTimeout() / 1000);
    }

    /**
     * 批量删除session
     * */
    public void doDelete(String sessionIds) {
        String[] sessionIds1 = sessionIds.split(",");
        for (String sessionId : sessionIds1) {
            doDelete(readSession(sessionId));
        }
    }
}