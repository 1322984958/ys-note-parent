package com.gkys.model.sys;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

public class SysPermission {
	/** id **/
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "version")
	private Integer version;
	/** 类别(1:目录,2:菜单,3:按钮) **/
	@Column(name = "category")
	private String category;
	/** 名称 **/
	@Column(name = "name")
	private String name;
	/** 值 **/
	@Column(name = "value")
	private String value;
	/** 父级id **/
	@Column(name = "parent_id")
	private Long parentId;
	/** 所属系统表id **/
	@Column(name = "system_id")
	private Long systemId;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	private Date createTime;
	/****/
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_modify_time")
	private Date lastModifyTime;
	/****/
	@Column(name = "description")
	private String description;
	/** 状态 **/
	@Column(name = "state")
	private String state;
	/** 路径 **/
	@Column(name = "uri")
	private String uri;
	/** 图标 **/
	@Column(name = "icon")
	private String icon;
	private Boolean open;
	private Boolean checked;
	/** 排序 **/
	@Column(name = "orders")
	private Integer orders;
	
	@Transient
	private List<SysPermission> childrens;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getSystemId() {
		return systemId;
	}

	public void setSystemId(Long systemId) {
		this.systemId = systemId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public List<SysPermission> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<SysPermission> childrens) {
		this.childrens = childrens;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}