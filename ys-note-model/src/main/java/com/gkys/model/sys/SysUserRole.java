package com.gkys.model.sys;

import javax.persistence.Column;
import javax.persistence.Id;

public class SysUserRole {
	/** id **/
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "version")
	private Integer version;
	/** 用户id **/
	@Column(name = "user_id")
	private Long userId;
	/** 角色id **/
	@Column(name = "role_id")
	private Long roleId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}