package com.gkys.model.sys;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class SysRolePermission {
	/** id **/
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "version")
	private Integer version;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;
	/****/
	@Column(name = "description")
	private String description;
	/** 角色id **/
	@Column(name = "role_id")
	private Long roleId;
	/** 类别 **/
	@Column(name = "category")
	private String category;
	/** 权限id **/
	@Column(name = "permission_id")
	private Long permissionId;

	public SysRolePermission(){}
	
	public SysRolePermission(Long roleId, Long permissionId) {
		this.roleId=roleId;
		this.permissionId=permissionId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}