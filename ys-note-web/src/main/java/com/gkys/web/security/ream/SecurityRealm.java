package com.gkys.web.security.ream;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gkys.api.sys.SysRoleService;
import com.gkys.api.sys.SysUserService;
import com.gkys.common.service.EncryptService;
import com.gkys.model.sys.SysPermission;
import com.gkys.model.sys.SysRole;
import com.gkys.model.sys.SysUser;

/**
 * 用户身份验证,授权 Realm 组件
 **/
@Component(value = "securityRealm")
public class SecurityRealm extends AuthorizingRealm {
	@Autowired
	private EncryptService passwordEncryptService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRoleService sysRoleService;

	/**
	 * 权限检查
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		String username = (String) principalCollection.getPrimaryPrincipal();
		SysUser sysUser = sysUserService.getUserByUsername(username);

		// 当前用户所有角色
		Set<SysRole> sysRoles = sysUserService.selectRolesByUserId(sysUser.getId());
		Set<String> rolesStr = new HashSet<String>();
		Set<String> permissionsStr = new HashSet<String>();
//		Set<String> roles = new HashSet<>();
		for (SysRole sysRole : sysRoles) {
			// 该角色下的权限
			if (StringUtils.isNotBlank(sysRole.getValue())) {
				rolesStr.add(sysRole.getValue());
				Set<SysPermission> roleSysPermissions = sysRoleService.selectPermissionsByRoleId(sysRole.getId());
				for(SysPermission sysPermission: roleSysPermissions){
					permissionsStr.add(sysPermission.getValue());
				}
			}
		}

		// 当前用户所有权限
		Set<SysPermission> sysPermissions = sysUserService.selectPermissionsByUserId(sysUser.getId());
//		Set<String> permissions = new HashSet<>();
		for (SysPermission sysPermission : sysPermissions) {
			if (StringUtils.isNotBlank(sysPermission.getValue())) {
				permissionsStr.add(sysPermission.getValue());
			}
		}

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(permissionsStr);
		simpleAuthorizationInfo.setRoles(rolesStr);
		return simpleAuthorizationInfo;
	}

	/**
	 * 登录验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken toToken = null;
		if ((token instanceof UsernamePasswordToken)) {
			toToken = (UsernamePasswordToken) token;
		}
		String username = (String) toToken.getPrincipal();
		SysUser record=new SysUser();
	    record.setUsername(username);
		final SysUser sysUser = sysUserService.selectOne(record);
		checkUserStatus(sysUser);
		SimpleAuthenticationInfo result = new SimpleAuthenticationInfo(username, sysUser.getPassword(), getName());
		if (!passwordEncryptService.saltDisabled()) {
			result.setCredentialsSalt(ByteSource.Util.bytes(sysUser.getSalt() + username));
		}
		return result;
	}

	private void checkUserStatus(SysUser sysUser) {
		if (sysUser == null) {
			throw new UnknownAccountException("登陆出错");
		}
		if ("1".equals(sysUser.getState())) {
			throw new LockedAccountException("当前账户被禁用。");
		}
		if(sysUser.getDeleteMark()==1){
			throw new AccountException("当前账户被删除。");
		}
	}

	protected String encryptPassword(String password, String salt, String userAccount) {
		return passwordEncryptService.encryptPassword(password, salt + userAccount);
	}

	/**
	 * 初始化密码加密匹配器
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(passwordEncryptService.getCredentialsStrategy()){
			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				UsernamePasswordToken toToken = null;
				if ((token instanceof UsernamePasswordToken)) {
					toToken = (UsernamePasswordToken) token;
				}
				if(toToken.isRememberMe()){
					Object accountCredentials = getCredentials(info);
					String pwd = String.valueOf(toToken.getPassword());
					System.out.println(pwd);
					System.out.println(accountCredentials);
					return true;
				}
				return super.doCredentialsMatch(token, info);
			}
		};
		matcher.setHashIterations(passwordEncryptService.getHashIterations());
		// System.out.println("加密的password = ["
		// +passwordEncryptService.encryptPassword("123456",
		// "92602949-c248-4fcc-952f-52e922996903" + "admin") + "]");
		setCredentialsMatcher(matcher);
	}

}
