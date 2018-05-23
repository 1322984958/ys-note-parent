package com.gkys.web.vo.security;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.gkys.model.sys.SysPermission;

public class SysPermissionVo {
	private Long id;
	/** 类别(1:目录,2:菜单,3:按钮) **/
	private String category;
	private SysSystemVo sysSystemVo;
	/** 名称 **/
	private String name;
	/** 值 **/
	private String value;
	/** 父级id **/
	private Long parentId;
	/** 所属系统表id **/
	private Long systemId;
	/** 创建时间 **/
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	/****/
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifyTime;
	/****/
	private String description;
	/** 状态 **/
	private String state;
	/** 路径 **/
	private String uri;
	/** 图标 **/
	@Column(name = "icon")
	private String icon;
	private Boolean open;
	private Boolean checked;
	/** 排序 **/
	private Integer orders;

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

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public List<SysPermission> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<SysPermission> childrens) {
		this.childrens = childrens;
	}

	public SysSystemVo getSysSystemVo() {
		return sysSystemVo;
	}

	public void setSysSystemVo(SysSystemVo sysSystemVo) {
		this.sysSystemVo = sysSystemVo;
	}

}
