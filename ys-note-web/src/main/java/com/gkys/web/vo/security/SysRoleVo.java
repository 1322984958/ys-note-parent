package com.gkys.web.vo.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class SysRoleVo {
	private Long id;
	/** 类别 **/
	private String category;
	private String systemId;
	private SysSystemVo sysSystemVo;
	private Integer version;
	/** 名称 **/
	private String name;
	/** 值 **/
	private String value;
	/****/
	@Column(name = "description")
	private String description;
	/** 状态 **/
	private String state;
	/** 排序 **/
	private Integer orders;

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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public SysSystemVo getSysSystemVo() {
		return sysSystemVo;
	}

	public void setSysSystemVo(SysSystemVo sysSystemVo) {
		this.sysSystemVo = sysSystemVo;
	}

}
